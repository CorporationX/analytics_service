package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/analytics")
    public List<AnalyticsEventDto> getAnalytics(@RequestBody AnalyticsEventFilterDto analyticsEventFilterDto) {
        return analyticsEventService.getAnalytics(analyticsEventFilterDto);
    }
}
