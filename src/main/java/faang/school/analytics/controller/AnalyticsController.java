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
        name = "Analytics API",
        description = "API for retrieving analytical data about events"
)
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Operation(
            summary = "Get event analytics",
            description = "Returns a list of events filtered by recipient, event type, and time range " +
                    "(either via intervalId or by explicitly specifying fromDate/toDate)."
    )
    @GetMapping("/analytics")
    public List<AnalyticsEventDto> getAnalytics(
            @Parameter(
                    description = "Recipient ID",
                    required = true,
                    example = "12345"
            )
            @RequestParam Long receiverId,

            @Parameter(
                    description = "Event type ID",
                    required = true,
                    example = "1"
            )
            @RequestParam int eventTypeId,

            @Parameter(
                    description = "Time interval ID (if not specified, fromDate/toDate are required)"
            )
            @RequestParam(required = false) Integer intervalId,

            @Parameter(
                    description = "Start date",
                    example = "2023-01-01T00:00:00"
            )
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,

            @Parameter(
                    description = "End date",
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
