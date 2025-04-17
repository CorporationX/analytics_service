package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "API аналитики",
        description = "API для получения аналитических данных о событиях"
)
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Operation(
            summary = "Получить аналитику событий",
            description = "Возвращает список событий с фильтрацией по получателю, типу события и временному диапазону " +
                    "(либо через intervalId, либо через явное указание fromDate/toDate)."
    )
    @GetMapping("/analytics")
    public List<AnalyticsEventDto> getAnalytics(
            @Parameter(
                    description = "ID получателя",
                    required = true,
                    example = "12345"
            )
            @RequestParam Long receiverId,

            @Parameter(
                    description = "ID типа события",
                    required = true,
                    example = "1"
            )
            @RequestParam int eventTypeId,

            @Parameter(
                    description = "ID временного интервала (если не указан, обязательны fromDate/toDate)"
            )
            @RequestParam(required = false) Integer intervalId,

            @Parameter(
                    description = "Начальная дата",
                    example = "2023-01-01T00:00:00"
            )
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,

            @Parameter(
                    description = "Конечная дата",
                    example = "2023-01-31T23:59:59"
            )
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        EventType eventType = EventType.of(eventTypeId);
        validateDateParametersAndInterval(intervalId, fromDate, toDate);
        if (intervalId != null) {
            Interval interval = Interval.of(intervalId);
            fromDate = interval.getStart();
            toDate = LocalDateTime.now();
        }
        return analyticsService.getAnalytics(receiverId, eventType, fromDate, toDate);
    }

    public void validateDateParametersAndInterval(Integer intervalId, LocalDateTime fromDate, LocalDateTime toDate) {
        if (intervalId == null && (fromDate == null || toDate == null)) {
            log.error("Validation failed: Neither intervalId nor valid date range (fromDate/toDate) provided");
            throw new DataValidationException("Either intervalId or both fromDate/toDate must be provided");
        }
    }
}
