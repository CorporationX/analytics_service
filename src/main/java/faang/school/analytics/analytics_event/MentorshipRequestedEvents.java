package faang.school.analytics.analytics_event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MentorshipRequestedEvents {
    private long senderId;
    private long receiverId;
    private long timestamp;

}