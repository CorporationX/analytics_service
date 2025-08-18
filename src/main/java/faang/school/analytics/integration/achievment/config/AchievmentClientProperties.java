package faang.school.analytics.integration.achievment.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.achievment-service")
public record AchievmentClientProperties(
        @NotBlank
        String host,
        @Positive
        Integer port,
        @NotBlank
        String getAchievmentUrl
) {
}
