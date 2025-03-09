package faang.school.analytics.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "analytics-service")
public class AnalyticsServiceProperties {
    private String apiVersion;
    private Redis redis = new Redis();

    @Data
    public static class Redis {
        private String host;
        private Integer port;
        private Channel channel;

        @Data
        public static class Channel {
            private String boughtPremiumTopic;
            private String recommendationEvent;
            private String filterUserEvent;
        }
    }
}