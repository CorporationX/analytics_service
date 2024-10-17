package faang.school.analytics.dto.analyticsEvent;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsEventDto {
    private long id;

    @NotNull(message = "Receiver ID cannot be null")
    @Positive(message = "Receiver ID must be positive")
    private long receiverId;

    @NotNull(message = "Actor ID cannot be null")
    @Positive(message = "Actor ID must be positive")
    private long actorId;

    @NotNull(message = "Event type cannot be null")
    private EventType eventType;

    @NotNull(message = "Received at cannot be null")
    private LocalDateTime receivedAt;
}
