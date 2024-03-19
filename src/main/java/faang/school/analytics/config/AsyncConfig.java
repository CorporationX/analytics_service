package faang.school.analytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Value("${async.core}")
    private int corePoolSize;
    @Value("${async.max}")
    private int maxSize;
    @Value("${async.queue_capacity}")
    private int queueCapacity;
    @Value("${async.prefix}")
    private String prefixThread;
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadNamePrefix(prefixThread);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}