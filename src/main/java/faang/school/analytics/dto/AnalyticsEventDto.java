package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnalyticsEventDto {
    long id;

    long receiverId;

    long actorId;

    EventType eventType;

    LocalDateTime receivedAt;
}
