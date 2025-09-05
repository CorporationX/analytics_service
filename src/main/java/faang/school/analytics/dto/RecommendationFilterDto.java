package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeIntervalType;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс с параметрами фильтрации
 *
 * @author Linempy
 * @since 20.08.2025
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class RecommendationFilterDto {

    @NonNull
    private Long id;

    @NonNull
    private EventType eventType;

    private TimeIntervalType timeType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @AssertTrue(message = "Нельзя одновременно указывать timeType и start/end время")
    public boolean isTimeTypeConsistent() {
        return timeType == null || (startTime == null && endTime == null);
    }

    @AssertTrue(message = "Начальное время должно быть раньше конечного")
    public boolean isTimeRangeValid() {
        return (startTime == null || endTime == null) || !startTime.isAfter(endTime);
    }
}