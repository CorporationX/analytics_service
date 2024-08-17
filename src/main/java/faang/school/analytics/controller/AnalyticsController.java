package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyticsEventDto> getAnalytics(@RequestParam Long receiverId,
                                                @RequestParam String eventType,
                                                @RequestParam(required = false) Optional<String> interval,
                                                @RequestParam(required = false) Optional<String> startDate,
                                                @RequestParam(required = false) Optional<String> endDate) {
        return analyticsEventService.getAnalytics(receiverId, eventType, interval, startDate, endDate);
    }
}
