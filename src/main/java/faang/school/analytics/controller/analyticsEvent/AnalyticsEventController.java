package faang.school.analytics.controller.analyticsEvent;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.exception.ExceptionMessages;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.analyticsEvent.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @PostMapping()
    public ResponseEntity<AnalyticsEventDto> saveEvent(@Valid @RequestBody AnalyticsEvent analyticsEvent) {
        return ResponseEntity.status(HttpStatus.OK).body(analyticsEventService.saveEvent(analyticsEvent));
    }

    @GetMapping("/{receiverId}")
    public ResponseEntity<List<AnalyticsEventDto>> getAnalyticsByInterval(
            @PathVariable("receiverId") long receiverId,
            @RequestParam @NonNull String eventString,
            @RequestParam(name = "intervalString", required = false) String intervalString,
            @RequestParam(name = "fromDateString", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String fromDateString,
            @RequestParam(name = "toDateString", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String toDateString) {

        log.info("Received request with eventString: {}, intervalString: {}, fromDateString: {}, toDateString: {}",
                eventString, intervalString, fromDateString, toDateString);

        EventType eventType = EventType.conversionToEventType(eventString);
        Interval interval = Interval.conversionToInterval(intervalString);

        LocalDateTime from = null;
        LocalDateTime to = null;

        if (interval == null) {
            if (fromDateString != null && toDateString != null) {
                from = LocalDateTime.parse(fromDateString);
                to = LocalDateTime.parse(toDateString);
            } else {
                log.error(ExceptionMessages.ARGUMENT_NOT_FOUND);
                throw new IllegalArgumentException(ExceptionMessages.ARGUMENT_NOT_FOUND);
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to));
    }
}