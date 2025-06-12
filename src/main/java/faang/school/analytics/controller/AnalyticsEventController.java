package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
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

    @GetMapping()
    List<AnalyticsEventDto> getAnalytics(
            @RequestParam long receiverId,
            @RequestParam String eventType,
            @RequestParam(required = false) String interval,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end
    ) {
        Interval analyticsInterval = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        EventType type;
        try {
            type = EventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Incorrect event type {} in analytics request for receiver {}", eventType, receiverId, e);
            throw new IllegalArgumentException("Incorrect event type.");
        }
        if (interval != null) {
            try {
                analyticsInterval = Interval.valueOf(interval.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Incorrect interval {} in analytics request for receiver {}", interval, receiverId, e);
                throw new IllegalArgumentException("Incorrect interval. Do not use \"interval\" parameter " +
                        "if you want to specify both start and end dates of the search");
            }
        } else {
            if (start == null || end == null) {
                log.error("Start or end date is null: {}, {} in analytics request for receiver {}",
                        start, end, receiverId);
                throw new IllegalArgumentException("if the \"interval\" parameter is missing," +
                        " both start and end dates of the search interval should be specified");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            startDate = parseDateTime(start, formatter, receiverId);
            endDate = parseDateTime(end, formatter, receiverId);
        }

        log.info("Start getting analytics for receiver {}, type {}, interval {}, start {}, end{}",
                receiverId, type, analyticsInterval, startDate, endDate);
        return analyticsEventService.getAnalytics(receiverId, type, analyticsInterval, startDate, endDate);
    }

    private LocalDateTime parseDateTime(String limit, DateTimeFormatter formatter, long receiverId) {
        LocalDateTime limitDate = null;
        try {
            limitDate = LocalDateTime.parse(limit, formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format {} in analytics request for receiver {}", limit, receiverId, e);
            throw new IllegalArgumentException(String.format("Invalid date format %s." +
                    " Format should be like yyyy-MM-dd HH:mm", limit));
        }
        return limitDate;
    }
}