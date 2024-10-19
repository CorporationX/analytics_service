package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipRequestReceivedDto {
    private long requestId;
    private long receiverId;
    private long actorId;
    private final EventType eventType = EventType.MENTORSHIP_REQUEST_RECEIVED;
    private LocalDateTime receivedAt;
}
