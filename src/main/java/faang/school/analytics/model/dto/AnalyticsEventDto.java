package faang.school.analytics.model.dto;

public record AnalyticsEventDto(
        long id,
        long receiverId,
        long actorId,
        String eventType,
        java.time.LocalDateTime receivedAt
) {
}
