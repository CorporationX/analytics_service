package faang.school.analytics.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
public class FollowerEvent {
    private String followerId;
    private String targetId;
    private LocalDateTime timestamp;
}

