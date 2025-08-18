package faang.school.analytics.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MentorshipRequestedEvent(long receiverId, long actorId, LocalDateTime receivedAt) {
}
