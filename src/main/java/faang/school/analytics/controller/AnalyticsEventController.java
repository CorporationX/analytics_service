package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/analytics")
    public List<AnalyticsEventDto> getAnalytics(@RequestBody AnalyticsEventFilterDto analyticsEventFilterDto) {
        return analyticsEventService.getAnalytics(analyticsEventFilterDto);
    }

    @PostMapping("/analytics")
    public void save(@RequestBody AnalyticsEvent analyticsEvent){
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
