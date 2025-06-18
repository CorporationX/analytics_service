package faang.school.analytics.kafka.events;

import faang.school.analytics.kafka.Event;
import faang.school.analytics.model.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public final class ProfileViewEvent extends Event {
    private EventType eventTypeEnum;
}
