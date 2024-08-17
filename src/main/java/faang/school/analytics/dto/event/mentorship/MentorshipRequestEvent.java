package faang.school.analytics.dto.event.mentorship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipRequestEvent {

    private UUID eventId;
    private long requesterId;
    private long receiverId;
    private LocalDateTime timestamp;
}