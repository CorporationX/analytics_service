package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/{idUser}")
    public AnalyticsDto getProfileViewsAnalytics(@PathVariable Long idUser, int eventTypeNumber,
                                                 @RequestParam(required = false) String interval,
                                                 @RequestParam(required = false) List<String> dates){
        EventType eventType = EventType.of(eventTypeNumber);
        Interval requiredInterval = null;
        if(!interval.isEmpty()){
            requiredInterval = Interval.get(interval);
        }
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if(!dates.isEmpty()){
            startDate = LocalDateTime.parse(dates.get(0));
            endDate = LocalDateTime.parse(dates.get(1));
        }
        return analyticsEventService.getAnalytics(idUser, eventType, requiredInterval, startDate, endDate);
    }
}
