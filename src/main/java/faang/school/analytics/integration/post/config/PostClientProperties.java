package faang.school.analytics.integration.post.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.post-service")
public record PostClientProperties(
        @NotBlank
        String host,
        @Positive
        Integer port,
        @NotBlank
        String getPostUrl
) {
}
