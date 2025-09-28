package faang.school.analytics.model.mentorshipevent;

import java.time.LocalDateTime;

public record MentorshipRequestedEvent(Long menteeId, Long mentorId, LocalDateTime requestTime) {
}
