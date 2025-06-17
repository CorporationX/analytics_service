package faang.school.analytics.kafka.events;

import faang.school.analytics.model.EventType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public final class ProfileViewEvent {
    private Long viewedUserId;
    private Long viewerUserId;
    private LocalDateTime localDateTime;
    private EventType eventType;
}
