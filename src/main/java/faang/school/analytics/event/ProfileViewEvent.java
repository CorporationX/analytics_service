package faang.school.analytics.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileViewEvent(
        long receiverId,
        long actorId,
        LocalDateTime receivedAt) {
}
