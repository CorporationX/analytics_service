package faang.school.analytics.dto;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeIntervalType;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * Класс с параметрами фильтрации
 *
 * @param id идентификатор сущности (обязательный параметр)
 * @param eventType тип ивента (обязательный параметр)
 * @param timeType тип интервала (необязательный параметр)
 * @param startTime дата и время начала (необязательный параметр)
 * @param endTime дата и время конца (необязательный параметр)
 *
 * @author Linempy
 * @since 20.08.2025
 */
public record RecommendationFilterDto(
        @NonNull Long id,
        @NonNull EventType eventType,
        TimeIntervalType timeType,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public void validate() {
        if (timeType != null && (startTime != null || endTime != null)) {
            throw new DataValidationException("Нельзя указывать одновременно timeType и startTime/endTime");
        }

        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new DataValidationException("Дата начала не может быть позже даты окончания");
        }
    }
}