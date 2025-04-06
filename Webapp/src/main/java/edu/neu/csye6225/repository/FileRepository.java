package edu.neu.csye6225.repository;

import edu.neu.csye6225.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}


