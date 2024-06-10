package faang.school.analytics.dto.profile;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileViewEventDto {
    private String userId;
    private String viewerId;
    private LocalDateTime viewedAt;
}
