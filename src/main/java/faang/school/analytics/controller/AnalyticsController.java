package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsCreateEventDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.GetAnalyticsRqDto;
import faang.school.analytics.service.AnalyticsService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/api/v1/analytics")
@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping
    public AnalyticsEventDto saveEvent(@RequestBody @Valid AnalyticsCreateEventDto createEventDto) {
        return analyticsService.saveEvent(createEventDto);
    }

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@Valid GetAnalyticsRqDto paramsDto) {
        return analyticsService.getAnalytics(paramsDto);
    }
}
