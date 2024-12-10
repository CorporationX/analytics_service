package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventDto {
    private Long id;

    @NotNull(message = "Receiver ID must not be null")
    @Positive(message = "Receiver ID must be positive")
    private long receiverId;

    @NotNull(message = "Actor ID must not be null")
    @Positive(message = "Actor ID must be positive")
    private long actorId;

    @NotNull(message = "EventType must not be null")
    private EventType eventType;

    @NotNull(message = "ReceivedAt must not be null")
    @PastOrPresent(message = "ReceivedAt must be in the past or present")
    private LocalDateTime receivedAt;
}
