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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        if(eventTypeName.isBlank() && eventTypeNumber == 0){
            throw new IllegalArgumentException("Select required event type");
        }
        if(!eventTypeName.isBlank() && eventTypeNumber > 0){
            throw new IllegalArgumentException("Select only one type parameter");
        }
        if(interval.isBlank() && dates.isEmpty()){
            throw new IllegalArgumentException("Select required time interval");
        }
        if(!interval.isBlank() && dates.size() <= 2 && dates.size() >= 1){
            throw new IllegalArgumentException("Select only interval parameter or start/end dates");
        }

        EventType eventType = null;
        if(eventTypeName.isBlank()) {
            eventType = EventType.of(eventTypeNumber);
        } else if(eventTypeNumber == 0){
            eventType = EventType.get(eventTypeName);
        }

        Interval requiredInterval = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if(dates.isEmpty()){
            requiredInterval = Interval.get(interval);
        }
        if(interval.isBlank()){
            startDate = LocalDateTime.parse(dates.get(0));
            endDate = LocalDateTime.parse(dates.get(1));
        }
        return analyticsEventService.getAnalytics(idUser, eventType, requiredInterval, startDate, endDate);
    }
}
