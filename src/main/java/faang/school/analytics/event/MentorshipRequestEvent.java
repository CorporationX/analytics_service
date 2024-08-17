package faang.school.analytics.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MentorshipRequestEvent {
    private long mentorId;
    private long menteeId;
    private LocalDateTime createdDate;
}
