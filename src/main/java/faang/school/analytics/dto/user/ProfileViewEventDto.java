package faang.school.analytics.dto.user;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewEventDto implements AnalyticsEventDto {

    private long receiverId;
    private long actorId;
    private final EventType eventType = EventType.PROFILE_VIEW;
    private LocalDateTime receivedAt;
}
