package faang.school.analytics.controller;

import faang.school.analytics.exceptions.InvalidEventTypeException;
import faang.school.analytics.exceptions.InvalidRequestException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static faang.school.analytics.constants.Constants.DATE_FORMAT;
import static faang.school.analytics.constants.Constants.INVALID_DATE_FORMAT;
import static faang.school.analytics.constants.Constants.INVALID_EVENT_TYPE;
import static faang.school.analytics.constants.Constants.MISSING_DATE_PARAMS;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final List<DateTimeFormatter> formatters = List.of(DateTimeFormatter.ofPattern(DATE_FORMAT),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    @GetMapping
    public ResponseEntity<List<AnalyticsEvent>> getAnalytics(@RequestParam long receiverId,
                                                             @RequestParam String eventType,
                                                             @RequestParam(required = false) String interval,
                                                             @RequestParam(required = false) String startDate,
                                                             @RequestParam(required = false) String endDate) {

        log.info("Received analytics request for receiverId={}, eventType={}, interval={}, startDate={}, endDate={}",
                receiverId, eventType, interval, startDate, endDate);

        EventType type = parseEventType(eventType);
        LocalDateTime start;
        LocalDateTime end;

        if (interval != null && interval.isBlank()) {
            Interval parcedInterval = parseInterval(interval);
            start = parcedInterval.getStartDate();
            end = parcedInterval.getEndDate();
            log.info("Parsed interval: start={}, end={}", start, end);
        } else if (startDate != null && startDate.isBlank() && endDate != null && endDate.isBlank()) {
            start = parseDate(startDate);
            end = parseDate(endDate);
            log.info("Parsed start and end dates: start={}, end={}", start, end);
        } else {
            log.warn("Invalid request: neither interval nor startDate/endDate provided.");
            throw new InvalidRequestException(MISSING_DATE_PARAMS);
        }

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, type, start, end);
        log.info("Analytics data returned: {} events", result.size());

        return ResponseEntity.ok(result);
    }

    private EventType parseEventType(String raw) {
        try {
            if (raw.matches("\\d+")) {
                return EventType.of(Integer.parseInt(raw));
            }
            return EventType.valueOf(raw.toUpperCase());
        } catch (Exception e) {
            log.error("Failed to parse event type: {}", raw, e);
            throw new InvalidEventTypeException(INVALID_EVENT_TYPE + raw);
        }
    }

    private Interval parseInterval(String raw) {
        log.info("Parsing interval: {}", raw);
        return Interval.from(raw);
    }

    private LocalDateTime parseDate(String dateStr) {
        log.info("Attempting to parse date: {}", dateStr);
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {}
        }
        log.error("Invalid date format: {}", dateStr);
        throw new InvalidRequestException(INVALID_DATE_FORMAT + dateStr);
    }
}

