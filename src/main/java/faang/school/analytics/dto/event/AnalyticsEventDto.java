package faang.school.analytics.dto.event;

import faang.school.analytics.model.EventType;
import faang.school.analytics.validator.analyticsEvent.eventType.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEventDto {

    private Long id;
    @NotNull(message = "Receiver id can't be empty")
    private Long receiverId;
    @NotNull(message = "Receiver id can't be empty")
    private Long actorId;
    @ValidEnum
    private EventType eventType;
    private LocalDateTime receivedAt;
}
