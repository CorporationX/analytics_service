package faang.school.analytics.event;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AnaliticsEvent {

    private long receiverId;

    private long actorId;

    private EventType eventType;

    private LocalDateTime receivedAt;
}
