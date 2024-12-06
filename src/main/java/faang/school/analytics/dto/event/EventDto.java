package faang.school.analytics.dto.event;

import faang.school.analytics.model.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    @NotNull(message = "receiverId must not be null")
    private long receiverId;
    @NotNull(message = "actorId must not be null")
    private long actorId;
    @NotNull(message = "eventType must not be null")
    private EventType eventType;
    private LocalDateTime receivedAt;
}
