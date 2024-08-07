package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyticsEventDto {
    private long id;
    @NotNull
    private long receiverId;
    @NotNull
    private long actorId;
    @NotNull
    private EventType eventType;
    @NotNull
    private LocalDateTime receivedAt;
}
