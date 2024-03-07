package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.ConvertAnalyticsParam;
import faang.school.analytics.validator.AnalyticsEventValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventValidator analyticsEventValidator;
    private final ConvertAnalyticsParam convertAnalyticsParam;

    @GetMapping("/events/{receiverId}/{eventType}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable @NotNull long receiverId,
                                                @PathVariable @NotNull String eventType,
                                                @RequestParam(required = false) String interval,
                                                @RequestParam(required = false) LocalDate fromDate,
                                                @RequestParam(required = false) LocalDate toDate) {

        analyticsEventValidator.validateRequest(interval, fromDate, toDate);
        @Nullable Interval intervalEnum = convertAnalyticsParam.convertInterval(interval);
        @Nullable LocalDateTime from = convertAnalyticsParam.convertFromDate(fromDate)
                .orElse(null);
        @Nullable LocalDateTime to = convertAnalyticsParam.convertToDate(toDate)
                .orElse(null);

        return analyticsEventService.getAnalytics(receiverId, EventType.valueOf(eventType),
                intervalEnum, from, to);
    }
}