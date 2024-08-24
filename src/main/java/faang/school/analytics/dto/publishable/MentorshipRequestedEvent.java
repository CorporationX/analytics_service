package faang.school.analytics.dto.publishable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorshipRequestedEvent {
    private Long requesterId;
    private Long receiverId;
    private LocalDateTime timestamp;
}
