package edu.neu.csye6225.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
public class S3Aspect {

    private final MeterRegistry meterRegistry;

    public S3Aspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(* com.amazonaws.services.s3.AmazonS3+.*(..))") // Apply only to AmazonS3 methods
    public Object logS3Metrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        Object result = joinPoint.proceed(); // Execute the S3 call
        Instant end = Instant.now();

        long duration = Duration.between(start, end).toMillis();

        // Get method name
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String s3Operation = signature.getMethod().getName();

        // Track S3 call execution time
        Timer.builder("s3_call_time")
                .description("Time taken for S3 call")
                .tag("operation", s3Operation)
                .register(meterRegistry)
                .record(Duration.ofMillis(duration));

        return result;
    }
}