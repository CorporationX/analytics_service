package faang.school.analytics.event;

import java.time.LocalDateTime;

/**
 * @author Alexander Bulgakov
 */

public record SearchAppearanceEvent(
        long receiverId,
        long actorId,
        LocalDateTime receivedAt
) {}
