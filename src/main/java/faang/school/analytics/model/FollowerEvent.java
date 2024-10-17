package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowerEvent {
    private Long followerId;
    private Long followedUserId;
    private Long followedProjectId;
    private LocalDateTime subscriptionTime;
}