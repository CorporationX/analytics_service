package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping
    List<AnalyticsEventDto> getAnalytics(
            @RequestParam long receiverId,
            @RequestParam String eventType,
            @RequestParam(required = false) String interval,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end
    ) {
        EventType type;
        try {
            type = EventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid values in analytics request", e);
            throw new DataValidationException("Invalid request value eventType");
        }

        Interval analyticsInterval = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (interval == null && (start == null || end == null)) {
            throw new DataValidationException("if the \"interval\" parameter is missing," +
                    " both start and end dates of the search interval should be specified");
        }
        if (interval != null) {
            try {
                analyticsInterval = Interval.valueOf(interval.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Invalid values in analytics request", e);
                throw new DataValidationException("Invalid request value interval");
            }
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                startDate = LocalDateTime.parse(start, formatter);
                endDate = LocalDateTime.parse(end, formatter);
            } catch (DateTimeParseException e) {
                log.error("Invalid date format in analytics request for receiver {}", receiverId, e);
                throw new DataValidationException("Invalid date format." +
                        " Format should be like yyyy-MM-dd HH:mm");
            }
        }
        log.debug("Start getting analytics for receiver {}, type {}, interval {}, start {}, end{}",
                receiverId, type, analyticsInterval, startDate, endDate);
        return analyticsEventService.getAnalytics(receiverId, type, analyticsInterval, startDate, endDate);
    }
}