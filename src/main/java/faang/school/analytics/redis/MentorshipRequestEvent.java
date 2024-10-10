package faang.school.analytics.redis;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MentorshipRequestEvent {

    private Long requesterId;
    private Long receiverId;
    private LocalDateTime requestTime;

}
