package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsEventService analyticsEventService;

    @PostMapping("/save")
    public void saveEvent(@Valid @RequestBody AnalyticsEventDto analyticsEventDto) {
        analyticsEventService.saveEvent(analyticsEventDto);
    }

    @PostMapping("/filters")
    public void getAnalyticsWithFilters(@Valid @RequestBody AnalyticsFilterDto analyticsFilterDto) {
        analyticsEventService.getAnalytics(analyticsFilterDto);
    }
}
