package edu.neu.csye6225.service;

import edu.neu.csye6225.model.HealthCheck;
import edu.neu.csye6225.repository.HealthCheckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    // Create a logger specific to this class
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckServiceImpl.class);

    @Autowired
    private HealthCheckRepository healthCheckRepository;

    @Override
    public boolean performHealthCheck() {
        try {
            // Log the start of the health check
            logger.info("Initiating health check at {}", Instant.now());

            HealthCheck healthCheck = new HealthCheck();

            // Log before saving
            logger.debug("Attempting to save health check record");

            healthCheckRepository.save(healthCheck);

            // Log successful health check
            logger.info("Health check completed successfully");

            return true;
        } catch (Exception e) {
            // Log the error with detailed information
            logger.error("Health check failed", e);

            // Optionally log specific error details
            logger.warn("Health check error details: {}", e.getMessage());

            return false;
        }
    }
}