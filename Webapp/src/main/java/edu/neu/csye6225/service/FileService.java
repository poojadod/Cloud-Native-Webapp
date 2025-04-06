package edu.neu.csye6225.service;

import edu.neu.csye6225.model.FileEntity;
import edu.neu.csye6225.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private final FileRepository fileRepository;

    @Autowired
    private final AmazonS3 s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public FileService(FileRepository fileRepository, AmazonS3 s3Client) {
        this.fileRepository = fileRepository;
        this.s3Client = s3Client;
    }

    public FileEntity storeFile(MultipartFile file) throws Exception {
        // Input validation logging
        if (file == null) {
            logger.warn("Attempt to store null file");
            throw new IllegalArgumentException("File cannot be null");
        }

        if (file.isEmpty()) {
            logger.warn("Attempt to store empty file");
            throw new Exception("File is empty");
        }

        // Log file details before processing
        logger.info("Attempting to store file: name={}, size={} bytes, content-type={}",
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType()
        );

        // Generating unique file name
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

        try {
            // Prepare S3 metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // Upload to S3
            s3Client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file.getInputStream(), metadata));

            // Generate file URL
            String fileUrl = s3Client.getUrl(bucketName, uniqueFileName).toString();
            logger.info("Successfully uploaded file to S3: bucket={}, fileName={}, fileUrl={}",
                    bucketName, uniqueFileName, fileUrl
            );

            // Prepare file entity
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(originalFileName);
            fileEntity.setFileUrl(fileUrl);
            fileEntity.setUploadDate(LocalDate.now().toString());

            try {
                // Save to database
                FileEntity savedEntity = fileRepository.save(fileEntity);
                logger.info("File metadata saved to database: fileId={}", savedEntity.getId());
                return savedEntity;
            } catch (Exception e) {
                // Rollback S3 upload if database save fails
                logger.error("Failed to save file metadata to database. Attempting to delete from S3", e);
                s3Client.deleteObject(bucketName, uniqueFileName);
                throw new RuntimeException("Error saving file to database: " + e.getMessage(), e);
            }
        } catch (IOException e) {
            logger.error("S3 upload failed for file: {}", originalFileName, e);
            throw new Exception("Error uploading file to S3: " + e.getMessage(), e);
        }
    }

    public Optional<FileEntity> getFile(UUID fileId) {
        logger.info("Attempting to retrieve file: fileId={}", fileId);
        Optional<FileEntity> file = fileRepository.findById(fileId);

        if (file.isPresent()) {
            logger.info("File found: fileName={}", file.get().getFileName());
        } else {
            logger.warn("No file found with ID: {}", fileId);
        }

        return file;
    }

    public void deleteFile(UUID fileId) throws Exception {
        logger.info("Attempting to delete file: fileId={}", fileId);

        Optional<FileEntity> fileEntity = fileRepository.findById(fileId);
        if (fileEntity.isPresent()) {
            String fileName = fileEntity.get().getFileName();
            String fileUrl = fileEntity.get().getFileUrl();

            try {
                // Extract file name from S3 URL if necessary
                String s3FileName = extractFileNameFromUrl(fileUrl);

                // Delete from S3
                s3Client.deleteObject(bucketName, s3FileName);
                logger.info("Deleted file from S3: fileName={}, bucketName={}", s3FileName, bucketName);

                // Delete from database
                fileRepository.deleteById(fileId);
                logger.info("Deleted file metadata from database: fileId={}", fileId);
            } catch (Exception e) {
                logger.error("Failed to delete file: fileId={}, fileName={}", fileId, fileName, e);
                throw new Exception("Error deleting file: " + e.getMessage(), e);
            }
        } else {
            logger.warn("Attempted to delete non-existent file: fileId={}", fileId);
            throw new Exception("File not found");
        }
    }

    // Utility method to extract filename from S3 URL
    private String extractFileNameFromUrl(String fileUrl) {
        try {
            return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        } catch (Exception e) {
            logger.error("Failed to extract filename from URL: {}", fileUrl, e);
            return null;
        }
    }
}