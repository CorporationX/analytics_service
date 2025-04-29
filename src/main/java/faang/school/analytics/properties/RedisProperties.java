package faang.school.analytics.properties;

import faang.school.analytics.until.EventType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private String host;
    private int port;
    private Map<String, String> topics;

    public String getTopic(@NonNull EventType eventType) {
        return topics.get(eventType.getKey());
    }
}
