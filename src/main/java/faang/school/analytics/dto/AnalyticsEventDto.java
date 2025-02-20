package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(
        @Positive long id,
        @Positive long receiverId,
        @Positive long actorId,
        EventType eventType,
        LocalDateTime receivedAt
) {}