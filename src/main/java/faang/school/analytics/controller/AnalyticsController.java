package faang.school.analytics.controller;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import faang.school.analytics.validator.AnalyticControllerValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsEventServiceImpl analyticsEventServiceImpl;
    private final AnalyticControllerValidator analyticControllerValidator;

    @GetMapping()
    @Operation(summary = "Get analytic", description = "Get analytic from DB")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyticsEventDto> getAnalytics(@RequestParam("id") Long id,
                                                @RequestParam("eventType") String eventType,
                                                @RequestParam(value = "interval", required = false) String interval,
                                                @RequestParam(value = "startDate", required = false) String startDate,
                                                @RequestParam(value = "endDate", required = false) String endDate) {

        analyticControllerValidator.validateIntervalAndDates(interval, startDate, endDate);
        Interval intervalRes = null;
        LocalDateTime startDateRes = null;
        LocalDateTime endDateRes = null;
        if (interval != null && !interval.isBlank()) {
            intervalRes = Interval.valueOf(interval.toUpperCase());
        } else {
            startDateRes = LocalDateTime.parse(startDate);
            endDateRes = LocalDateTime.parse(endDate);
        }
        EventType eventTypeRes = EventType.valueOf(eventType.toUpperCase());
        List<AnalyticsEventDto> result = analyticsEventServiceImpl.getAnalytics(id, eventTypeRes, intervalRes, startDateRes, endDateRes);
        System.out.printf("result " + result);
        return result;

    }
}
