package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.handler.BadRequestException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Evgenii Malkov
 */
@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticController {

    public final AnalyticsEventService analyticsEventService;

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@RequestParam long id,
                                                @RequestParam EventType eventType,
                                                @RequestParam(required = false) Interval interval,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new BadRequestException("No period filter value specified");
        }
        return analyticsEventService.getAnalytics(id, eventType, interval, from, to);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Validated AnalyticsEventDto analyticsEventDto) {
        analyticsEventService.saveEvent(analyticsEventDto);
    }
}
