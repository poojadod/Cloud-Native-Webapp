package edu.neu.csye6225.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
public class APIAspect {

    @Autowired
    private final MeterRegistry meterRegistry;

    public APIAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(* edu.neu.csye6225.controller..*(..))")
    public Object logApiMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        String endpoint = "UNKNOWN";
        String method = "UNKNOWN";

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            method = request.getMethod();
            Object bestMatchAttr = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            String pattern = bestMatchAttr != null ? bestMatchAttr.toString() : request.getRequestURI();

            endpoint = method + " " + pattern;
        }

        // Counts the API calls
        meterRegistry.counter("api_calls", "endpoint", endpoint).increment();

        // Track response time
        Timer.builder("api_response_time")
                .description("Time taken for API response")
                .tag("endpoint", endpoint)
                .register(meterRegistry)
                .record(Duration.ofMillis(duration));

        return result;
    }
}