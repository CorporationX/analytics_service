package faang.school.analytics.model.event.SearchAppearance;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record InputSearchAppearanceEvent(
        Long requesterId,
        List<Long> foundUsersId,
        LocalDateTime requestDateTime
) {
}
