package faang.school.analytics.message.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileViewEvent(
        long actorId,
        long receiverId,
        LocalDateTime receivedAt
) {
}
