package faang.school.analytics.dto;

import java.time.LocalDateTime;

/**
 * Класс-ивент рекомендации для сервиса аналитики
 *
 * @param requesterId идентификатор отправителя
 * @param receiverId идентификатор получателя
 * @param recommendationId идентификатор рекомендации
 * @param receivedAt время и дата создания рекомендации
 *
 * @author Linempy
 * @since 20.08.2025
 */
public record RecommendationEvent(
        Long requesterId,
        Long receiverId,
        Long recommendationId,
        LocalDateTime receivedAt
) {
}