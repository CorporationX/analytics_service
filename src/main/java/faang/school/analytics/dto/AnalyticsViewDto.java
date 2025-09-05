package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;

/**
 * DTO для сущности аналитики
 *
 * @param id ID аналитики
 * @param actorId ID отправителя
 * @param receiverId ID получателя
 * @param eventType тип ивента
 * @param receivedAt время сохранения аналитики
 *
 * @author Linempy
 * @since 04.09.2025
 */
public record AnalyticsViewDto(
        Long id,
        Long actorId,
        Long receiverId,
        EventType eventType,
        LocalDateTime receivedAt
) {
}