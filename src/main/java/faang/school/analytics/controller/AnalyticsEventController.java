package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService service;

    @GetMapping("/{id}/type/{type}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable long id,
                                                @PathVariable EventType type,
                                                @RequestParam("startDate") LocalDateTime startDate,
                                                @RequestParam("endDate") LocalDateTime endDate) {
        return service.getAnalytics(id, type, startDate, endDate);
    }
}
