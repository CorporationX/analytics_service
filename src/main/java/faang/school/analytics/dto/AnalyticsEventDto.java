package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsEventDto {

    @Positive(message = "Id should be positive")
    private Long id;

    @NotNull(message = "ReceiverId should not be null")
    @Positive(message = "ReceiverId should be positive")
    private Long receiverId;

    @NotNull(message = "ActorId should not be null")
    @Positive(message = "ActorId should be positive")
    private Long actorId;

    @NotNull(message = "EventType should not be null")
    private EventType eventType;

    @NotNull(message = "ReceivedAt should not be null")
    private LocalDateTime receivedAt;
}
