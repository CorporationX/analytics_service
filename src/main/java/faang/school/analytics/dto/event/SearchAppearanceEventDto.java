package faang.school.analytics.dto.event;

import java.time.LocalDateTime;

/**
 * @author Alexander Bulgakov
 */

public record SearchAppearanceEventDto(
        long receiverId,
        long actorId,
        LocalDateTime receivedAt
) {}
