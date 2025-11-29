package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FollowerEvent {
    private long followerId;
    private long followeeId;
    private LocalDateTime timestamp;
}