package faang.school.analytics.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipRequestEvent {
    private long requesterId;
    private long receiverId;
    private LocalDateTime timestamp;
}
