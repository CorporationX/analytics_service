package faang.school.analytics.parser;

import faang.school.analytics.enums.EventType;
import faang.school.analytics.exceptions.InvalidEventTypeException;
import faang.school.analytics.exceptions.InvalidRequestException;
import faang.school.analytics.model.Interval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static faang.school.analytics.constants.Constants.DATE_FORMAT;
import static faang.school.analytics.constants.Constants.INVALID_DATE_FORMAT;
import static faang.school.analytics.constants.Constants.INVALID_EVENT_TYPE;

@Component
@Slf4j
public class AnalyticsRequestParser {
    private static final List<DateTimeFormatter> FORMATTERS = List.of(DateTimeFormatter.ofPattern(DATE_FORMAT),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    public EventType parseEventType(String raw) {
        try {
            if (raw.matches("\\d+")) {
                int code = Integer.parseInt(raw);
                return EventType.of(code);
            }
            return EventType.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Failed to parse event type: {}", raw, e);
            throw new InvalidEventTypeException(INVALID_EVENT_TYPE + raw);
        }
    }

    public Interval parseInterval(String raw) {
        log.info("Parsing interval: {}", raw);
        return Interval.from(raw);
    }

    public LocalDateTime parseDate(String dateStr) {
        log.info("Attempting to parse date: {}", dateStr);
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                log.debug("Failed to parse with formatter {}: {}", formatter, e.getMessage());
            }
        }
        log.error("Invalid date format: {}", dateStr);
        throw new InvalidRequestException(INVALID_DATE_FORMAT + dateStr);
    }
}
