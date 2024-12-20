package faang.school.analytics.dto.event.analyticsEvent;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record AnalyticsEventRequestDto(
        @Positive long receiverId,
        @Positive long actorId
) {
}
