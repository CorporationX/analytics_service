package faang.school.analytics.event.mentorship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipRequestedEvent {

    private long requesterId;
    private long receiverId;
    private LocalDateTime requestedAt;
}