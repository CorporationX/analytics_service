package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/{idUser}")
    public AnalyticsDto getProfileViewsAnalytics(@PathVariable Long idUser,
                                                 @RequestParam(required = false, defaultValue = "0") Integer eventTypeNumber,
                                                 @RequestParam(required = false, defaultValue = "") String eventTypeName,
                                                 @RequestParam(required = false, defaultValue = "") String interval,
                                                 @RequestParam(required = false, defaultValue = "") List<String> dates){
        return analyticsEventService.getAnalytics(idUser, eventTypeNumber, eventTypeName, interval, dates);
    }
}
