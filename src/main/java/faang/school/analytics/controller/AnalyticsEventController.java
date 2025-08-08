package faang.school.analytics.controller;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.request.GetAnalyticsRequestDto;
import faang.school.analytics.service.AnalyticsEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Validated
@Tag(name = "Analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @Operation(summary = "Get analytics events according to query parameters")
    @GetMapping
    public List<AnalyticsEventDto> get(
            @RequestParam GetAnalyticsRequestDto getAnalyticsRequestDto
            ) {
        return analyticsEventService.getAnalytics(getAnalyticsRequestDto);
    }
}
