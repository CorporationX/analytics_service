package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfileViewDto {
    private long userId;
    private long viewerId;
    private LocalDateTime viewedAt;
}
