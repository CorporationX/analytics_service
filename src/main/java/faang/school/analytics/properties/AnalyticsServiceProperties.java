package faang.school.analytics.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "analytics-service")
@Getter
@Setter
public class AnalyticsServiceProperties {
    private Redis redis;

    @Getter
    @Setter
    public static class Redis {
        private String analyticsTopic;
    }
}
