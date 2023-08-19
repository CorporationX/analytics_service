package faang.school.analytics.controller;


import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService service;

    @GetMapping("/{id}/type/{type}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable long id,
                                                @PathVariable int type,
                                                @RequestParam("startDate") LocalDateTime startDate,
                                                @RequestParam("endDate") LocalDateTime endDate) {
        List<AnalyticsEventDto> result = service.getAnalytics(id, type, startDate, endDate);
        return result;
    }
}
