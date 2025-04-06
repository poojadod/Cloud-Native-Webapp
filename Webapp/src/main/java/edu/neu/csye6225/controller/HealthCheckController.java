package edu.neu.csye6225.controller;

import edu.neu.csye6225.service.HealthCheckService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthCheckController {

    // Create a logger specific to this class
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping("/healthz")
    public ResponseEntity<Void> healthCheck(HttpServletRequest request) {
        // Log the incoming health check request
        logger.info("Received health check request from IP: {}", request.getRemoteAddr());

        // Check if the request has a payload
        if (request.getContentLength() > 0 || !request.getParameterMap().isEmpty()) {
            logger.warn("Health check request contains unexpected payload from IP: {}", request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            boolean isHealthy = healthCheckService.performHealthCheck();

            if (isHealthy) {
                logger.info("Health check successful from IP: {}", request.getRemoteAddr());
                return ResponseEntity.ok().build();
            } else {
                logger.warn("Health check failed from IP: {}", request.getRemoteAddr());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
        } catch (Exception e) {
            logger.error("Unexpected error during health check from IP: {}", request.getRemoteAddr(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/healthz", method = {
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH,
            RequestMethod.HEAD,
            RequestMethod.OPTIONS,
            RequestMethod.TRACE
    })
    public ResponseEntity<Void> handleUnsupportedMethods(HttpServletRequest request) {
        // Log unsupported method attempts
        logger.warn("Unsupported HTTP method used for health check from IP: {} with method: {}",
                request.getRemoteAddr(),
                request.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    // Catch-all mapping for any other paths
    @RequestMapping("/**")
    public ResponseEntity<Void> handleInvalidPaths(HttpServletRequest request) {
        // Log invalid path attempts
        logger.warn("Invalid path access attempt from IP: {} to path: {}",
                request.getRemoteAddr(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}