package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record MentorshipRequestedEvent(Long requesterId, Long receiverId, LocalDateTime createdAt) {
}

