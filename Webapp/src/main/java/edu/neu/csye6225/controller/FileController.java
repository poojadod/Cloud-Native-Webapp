package edu.neu.csye6225.controller;

import edu.neu.csye6225.model.FileEntity;
import edu.neu.csye6225.service.FileService;
import edu.neu.csye6225.service.HealthCheckService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/file")
public class FileController {
    // Create a logger specific to this class
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private final FileService fileService;

    @Autowired
    private HealthCheckService healthCheckService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    private ResponseEntity<Void> checkHealth() {
        try {
            boolean isHealthy = healthCheckService.performHealthCheck();
            if (!isHealthy) {
                logger.warn("Health check failed. Service is unavailable.");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
            logger.debug("Health check passed successfully.");
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error during health check", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<FileEntity> uploadFile(
            @RequestParam(value = "profilePic", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        // Log the incoming file upload request
        logger.info("Received file upload request");

        // Perform health check first
        ResponseEntity<Void> healthCheckResponse = checkHealth();
        if (healthCheckResponse != null) {
            logger.warn("File upload request blocked due to failed health check");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }

        // Validate file
        if (file == null || file.isEmpty()) {
            logger.warn("File upload request with empty or null file");
            return ResponseEntity.badRequest().build();
        }

        try {
            // Log file details before storing
            logger.info("Uploading file: name={}, size={} bytes",
                    file.getOriginalFilename(),
                    file.getSize()
            );

            FileEntity savedFile = fileService.storeFile(file);

            logger.info("File uploaded successfully: id={}", savedFile.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
        } catch (Exception e) {
            // Log the full exception with stack trace
            logger.error("Error during file upload", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileEntity> getFile(@PathVariable UUID id, HttpServletRequest request) {
        // Log the file retrieval attempt
        logger.info("Received file retrieval request for id: {}", id);

        // Validate request
        if (request.getContentLength() > 0 || !request.getParameterMap().isEmpty()) {
            logger.warn("Invalid request parameters for file retrieval: id={}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Optional<FileEntity> file = fileService.getFile(id);

            if (file.isPresent()) {
                logger.info("File retrieved successfully: id={}", id);
                return ResponseEntity.ok(file.get());
            } else {
                logger.warn("File not found: id={}", id);
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            logger.error("Error retrieving file: id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable UUID id, HttpServletRequest request) {
        // Log the file deletion attempt
        logger.info("Received file deletion request for id: {}", id);

        // Validate request
        if (request.getContentLength() > 0 || !request.getParameterMap().isEmpty()) {
            logger.warn("Invalid request parameters for file deletion: id={}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            fileService.deleteFile(id);
            logger.info("File deleted successfully: id={}", id);
            return ResponseEntity.status(204).body("No Content");
        } catch (Exception e) {
            logger.error("Error deleting file: id={}", id, e);
            return ResponseEntity.status(404).body(null);
        }
    }

    // Existing unsupported methods remain the same...
    @RequestMapping(method = {
            RequestMethod.GET,
            RequestMethod.DELETE,
    })
    public ResponseEntity<Void> handleUnSupportedRequests() {
        logger.warn("Unsupported request method received");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @RequestMapping(method = {
            RequestMethod.HEAD,
            RequestMethod.OPTIONS,
            RequestMethod.PATCH,
            RequestMethod.PUT
    })
    public ResponseEntity<Void> handleUnsupportedMethodsOnFile() {
        logger.warn("Unsupported HTTP method received for file endpoint");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    @RequestMapping(value = "/{id}", method = {
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.PATCH,
            RequestMethod.HEAD,
            RequestMethod.OPTIONS
    })
    public ResponseEntity<Void> handleUnsupportedMethods() {
        logger.warn("Unsupported HTTP method received for specific file resource");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
}