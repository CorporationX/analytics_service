package faang.school.analytics.dto.follower;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FollowerEventDto {
    private long followerId;
    private long followeeId;
    private LocalDateTime timestamp;
}
