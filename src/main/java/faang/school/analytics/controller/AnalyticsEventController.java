package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final AnalyticsEventValidator analyticsEventValidator;
    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public List<AnalyticEventDto> getAnalytics(@RequestBody AnalyticInfoDto analyticInfoDto) {

        analyticsEventValidator.validate(analyticInfoDto);

        return analyticsEventService.getAnalytics(analyticInfoDto);
    }
}
