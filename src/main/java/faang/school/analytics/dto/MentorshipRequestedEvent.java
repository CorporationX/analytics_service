package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipRequestedEvent {
    private long requesterId;
    private long receiverId;
    private LocalDateTime receivedAt;
}
