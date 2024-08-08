package faang.school.analytics.controller.analyticsEvent;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.filters.timeManagment.Interval;
import faang.school.analytics.filters.timeManagment.TimeRange;
import faang.school.analytics.model.EventType;
import faang.school.analytics.services.AnalyticsEventService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/{receiverId}")
    public List<AnalyticsEventDto> getAnalyticsEvent(
            @RequestParam(name = "receiverId", required = true) long receiverId
            , @RequestParam(name = "eventType", required = true) String eventType
            , @RequestParam(name = "interval", defaultValue = "", required = false) String interval
            , @RequestParam(name = "from", defaultValue = "", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Validated @NotBlank String from
            , @RequestParam(name = "to", defaultValue = "", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Validated @NotBlank String to) {
        TimeRange timeRange = new TimeRange();
        timeRange.setStart(toLocalDateTime(from));
        timeRange.setEnd(toLocalDateTime(to));
        validateTimeRange(Interval.of(interval), timeRange);
        return analyticsEventService.getAnalytics(receiverId, EventType.of(eventType), Interval.of(interval), toLocalDateTime(from), toLocalDateTime(to));
    }

    private LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try {
            if (date == null || date.isEmpty()) return null;
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            log.error("invalid date format ", e);
            throw new DateTimeParseException("invalid date format. Try 'dd-MM-yyyy HH:mm:ss'",date,0);
        }
    }

    private void validateTimeRange(Interval interval, TimeRange timeRange) {
        if (interval == null && timeRange.isEmpty()) {
            throw new IllegalArgumentException("You must specify one of intervals");
        } else if (interval == null && !timeRange.isCompletelyFilled()) {
            throw new IllegalArgumentException("Custom interval must be completely filled");
        } else if (interval != null && timeRange.isCompletelyFilled()) {
            throw new IllegalArgumentException("Not allowed to specify both intervals");
        }
    }
}
