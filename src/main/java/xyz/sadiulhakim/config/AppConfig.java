package xyz.sadiulhakim.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class AppConfig {

    @Bean
    ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TaskScheduler scheduledTaskScheduler() {

        // Define a dynamic thread pool size based on available system resources
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int poolSize = Math.max(availableProcessors, 10); // Ensure at least 10 threads

        ScheduledExecutorService scheduledVirtualExecutor = Executors.newScheduledThreadPool(poolSize, Thread.ofPlatform().factory());
        return new ConcurrentTaskScheduler(scheduledVirtualExecutor);
    }

}
