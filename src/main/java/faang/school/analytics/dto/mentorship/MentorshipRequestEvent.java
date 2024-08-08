package faang.school.analytics.dto.mentorship;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MentorshipRequestEvent {
    private long requesterId;
    private long receiverId;
    private LocalDateTime receivedAt;
}
