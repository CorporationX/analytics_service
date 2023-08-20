package faang.school.analytics.dto.followEvent;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowEventDto {
    private Long subscriberId;
    private Long targetUserId;
    private Long projectId;
    private EventType eventType = EventType.FOLLOWER;
    private LocalDateTime subscriptionDateTime;
}
