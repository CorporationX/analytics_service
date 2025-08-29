package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MentorshipRequestedEvent {
    private long senderId;
    private long receiverId;
    private long timestamp;

}