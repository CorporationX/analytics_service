package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionEvent {
    private Long followerId;
    private Long followeeId;
    private LocalDateTime subscribedAt;
}