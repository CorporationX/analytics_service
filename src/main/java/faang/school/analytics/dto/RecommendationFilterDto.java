package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeIntervalType;
import jakarta.validation.constraints.AssertTrue;
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
    @AssertTrue
    public boolean validate() {
        boolean hasTimeConflict = timeType != null && (startTime != null || endTime != null);
        boolean hasInvalidTimeRange = startTime != null && endTime != null && startTime.isAfter(endTime);

        return !hasTimeConflict && !hasInvalidTimeRange;
    }
}