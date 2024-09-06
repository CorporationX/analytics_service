package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipRequestEvent {
    private long mentorId;
    private long menteeId;
    private LocalDateTime createdDate;
}
