package faang.school.analytics.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record MentorshipRequestedEvent(
        @NotNull
        @Positive
        Long MentorshipRequestSenderId,
        @NotNull
        @Positive
        Long MentorId,
        LocalDateTime timestamp
) {
}
