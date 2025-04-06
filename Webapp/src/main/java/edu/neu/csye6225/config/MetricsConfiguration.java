package edu.neu.csye6225.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Metrics;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import io.micrometer.statsd.StatsdProtocol;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdMeterRegistry;

import java.time.Duration;


@Configuration
public class MetricsConfiguration {
    @Bean
    public MeterRegistry meterRegistry() {
        return new StatsdMeterRegistry(new StatsdConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String prefix() {
                return "webapp";
            }

            @Override
            public StatsdProtocol protocol() {
                return StatsdProtocol.UDP;
            }

            @Override
            public String host() {
                return "localhost";
            }

            @Override
            public int port() {
                return 8125;
            }
        }, Clock.SYSTEM);
    }
}