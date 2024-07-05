package faang.school.analytics.model.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionEvent extends Event {
    private Long projectId;
    private Long followerId;
    private Long followeeId;
    private SubscriptionEventType eventType;
}
