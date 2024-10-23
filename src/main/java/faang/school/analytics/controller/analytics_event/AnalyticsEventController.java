package faang.school.analytics.controller.analytics_event;

import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.analytics_event.AnalyticsEventGetDto;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/analytics")
@Tag(name = "Analytics Controller",
        description = "Gives capability to get various analytics from other services")
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @Operation(summary = "Get analytics from DB",
            description = "Returns list of Analytics Events " +
                    "provided by required in body receiverId, eventType and various time")
    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@Valid @RequestBody AnalyticsEventGetDto analyticsEventGetDto) {
        return analyticsEventService.getAnalytics(analyticsEventGetDto);
    }
}
