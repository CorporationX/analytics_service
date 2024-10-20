package faang.school.analytics.model.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MentorshipRequestedEvent(
        long userId,
        long receiverId,
        LocalDateTime requestedAt
) {
}
