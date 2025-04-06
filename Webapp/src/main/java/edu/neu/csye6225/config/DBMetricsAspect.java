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
public class DBMetricsAspect {

    private final MeterRegistry meterRegistry;

    public DBMetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(* edu.neu.csye6225.repository..*(..))")
    public Object logDatabaseQueryMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        Object result = joinPoint.proceed(); // Execute the database query
        Instant end = Instant.now();

        long duration = Duration.between(start, end).toMillis();

        // Get method name (repository method)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String queryMethod = signature.getMethod().getName();

        // Track database query execution time
        Timer.builder("db_query_time")
                .description("Time taken for database queries")
                .tag("query", queryMethod)
                .register(meterRegistry)
                .record(Duration.ofMillis(duration));

        return result;
    }
}