package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsEventDto {
    private long id;
    @NotNull(message = "Receiver id field must contain some value")
    private long receiverId;
    @NotNull(message = "Actor id field must contain some value")
    private long actorId;
    @NotNull(message = "Event type field must contain some value")
    private EventType eventType;
    @NotNull(message = "Date of receiving event must contain some value")
    private LocalDateTime receivedAt;
}
