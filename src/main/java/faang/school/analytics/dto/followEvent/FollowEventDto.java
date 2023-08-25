package faang.school.analytics.dto.followEvent;

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
    private LocalDateTime subscriptionDateTime;
}
