package faang.school.analytics.controller;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<AnalyticsEventDto> getAnalyticsEvent(AnalyticsFilterDto analyticsFilterDto) {

        return analyticsEventService.getAnalytics(analyticsFilterDto);
    }
}
