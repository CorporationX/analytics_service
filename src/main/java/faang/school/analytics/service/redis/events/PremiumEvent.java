package faang.school.analytics.service.redis.events;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumEvent implements Serializable {
    private EventType eventType;
    private Date receivedAt;
    private Long userId;
}
