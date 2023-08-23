package faang.school.analytics.dto.redis;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnalyticsEventDto {
    private long userId;
    private long receiverId;
    private long actorId;
    private LocalDateTime viewedAt;
}
