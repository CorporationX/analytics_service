package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.EnumConverter;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventValidator analyticsEventValidator;

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(
            @RequestParam long receiverId,
            @RequestParam String eventType,
            @RequestParam(required = false) String interval,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        analyticsEventValidator.validatePresenceOfIntervalOrDateRange(interval, from, to);

        return analyticsEventService.getAnalytics(
                receiverId,
                EnumConverter.fromValue(EventType.class, eventType),
                EnumConverter.fromValue(Interval.class, interval),
                from,
                to
        );
    }
}
