package faang.school.analytics.dto.event.projectViewEvent;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectViewEvent(
        long projectId,
        long userId,
        LocalDateTime receivedAt
) {
}
