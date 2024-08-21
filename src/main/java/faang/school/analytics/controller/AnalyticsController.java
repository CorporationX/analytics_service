package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.service.AnalyticsService;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final AnalyticsEventValidator analyticsEventValidator;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyticsEventDto> getAnalytics(@RequestParam Long eventId,
                                                @RequestParam Integer eventTypeOrdinal,
                                                @RequestParam(required = false) String intervalStr,
                                                @RequestParam(required = false) String fromStr,
                                                @RequestParam(required = false) String toStr) {
        analyticsEventValidator.validateTimeBoundsPresence(intervalStr, fromStr, toStr);
        return analyticsService.getAnalytics(eventId, eventTypeOrdinal, intervalStr, fromStr, toStr);
    }
}
