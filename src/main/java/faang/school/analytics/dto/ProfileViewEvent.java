package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileViewEvent {
    private Long viewerId;
    private Long userProfileId;
    private LocalDateTime viewDate;
}
