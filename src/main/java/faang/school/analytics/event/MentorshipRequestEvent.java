package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipRequestEvent {
    private Long requesterId;
    private Long receiverId;
    private LocalDateTime requestTime;
}
