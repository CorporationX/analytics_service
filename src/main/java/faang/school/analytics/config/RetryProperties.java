package faang.school.analytics.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "spring.retry")
public class RetryProperties {
    @Min(3)
    private int maxAttempts;
    @Min(1000)
    private long initialDelay;
    @Min(1)
    private int multiplier;
    @Min(1000)
    private long maxDelay;
}