package faang.school.analytics.dto.analyticsEvent;

import faang.school.analytics.model.EventType;
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
    @NotNull
    private Long receiverId;
    @NotNull
    private Long actorId;
    @NotNull
    private EventType eventType;
    private LocalDateTime receivedAt;
}
