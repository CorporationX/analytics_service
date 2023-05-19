package faang.school.analytics.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfileViewEvent {
    private long userId;
    private long viewerId;
    private LocalDateTime viewedAt;
}
