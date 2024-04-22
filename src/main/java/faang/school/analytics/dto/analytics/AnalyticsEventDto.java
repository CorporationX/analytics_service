package faang.school.analytics.dto.analytics;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
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

    private long id;

    @Positive(message = "receiverId must be positive number")
    private long receiverId;

    @Positive(message = "actorId must be positive number")
    private long actorId;

    @NotNull(message = "eventType can't be null")
    private EventType eventType;

    private LocalDateTime receivedAt;
}
