package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.dto.AnalyticsEventRequestDTO;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
@Slf4j
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping
    public List<AnalyticsEventDTO> getAnalytics(@RequestBody AnalyticsEventRequestDTO analyticsEventRequestDTO) {
        log.info("Was received request on event analytics: {}", analyticsEventRequestDTO);
        return analyticsEventService.getAnalytics(analyticsEventRequestDTO);
    }
}
