package faang.school.analytics.contoller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.ValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.validator.AnalyticsEventControllerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final AnalyticsEventControllerValidator validator;
    private final AnalyticsEventService service;

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@RequestParam long id,
                                                @RequestParam("event_type") EventType eventType,
                                                @RequestParam(required = false) Interval interval,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                LocalDateTime from,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                LocalDateTime to) {

        checkIntervalAndFromTo(interval, from, to);
        if (validator.validateFromToNotNull(from, to)) {
            checkFromNotAfterTo(from, to);
        }

        return service.getAnalytics(id, eventType, interval, from, to);
    }

    private void checkIntervalAndFromTo(Interval interval, LocalDateTime from, LocalDateTime to) {
        boolean bothNotNull = validator.validateIntervalNotNull(interval)
                && validator.validateFromToNotNull(from, to);
        boolean bothNull = !validator.validateIntervalNotNull(interval)
                && !validator.validateFromToNotNull(from, to);

        if (bothNull || bothNotNull) {
            throw new ValidationException(
                    "Either the interval or both date parameters must be passed."
            );

        }
    }

    private void checkFromNotAfterTo(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new ValidationException(String.format(
                    "Value 'from' [%s] should be before or equal to value 'to [%s]",
                    from.format(DateTimeFormatter.ISO_DATE_TIME),
                    to.format(DateTimeFormatter.ISO_DATE_TIME)));
        }
    }
}
