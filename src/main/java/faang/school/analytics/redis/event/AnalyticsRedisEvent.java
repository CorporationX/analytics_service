package faang.school.analytics.redis.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsRedisEvent {
    private String type;
    private Map<String, Object> data;
}
