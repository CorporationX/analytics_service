package faang.school.analytics.model.event.SearchAppearance;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchAppearanceEvent(
        Long requesterId,
        Long foundUserId,
        LocalDateTime requestDateTime
) {
}
