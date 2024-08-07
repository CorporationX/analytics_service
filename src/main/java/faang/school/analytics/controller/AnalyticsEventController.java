package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@Positive @RequestParam Long receiverId,
                                                @NotBlank @RequestParam String typeEvent,
                                                @RequestParam String interval,
                                                @RequestParam String dateFrom,
                                                @RequestParam String dateTo) throws ParseException {

        EventType eventType = EventType.valueOf(typeEvent.toUpperCase());
        Interval foundInterval = Interval.valueOf(interval.toUpperCase());
        LocalDateTime from = LocalDateTime.parse(dateFrom);
        LocalDateTime to = LocalDateTime.parse(dateTo);
        return analyticsEventService.getAnalytics(receiverId, eventType, foundInterval, from, to);
    }

//    @GetMapping
//    public List<AnalyticsEventDto> getAnalytics(@Positive @RequestParam Long receiverId,
//                                                @NotBlank @RequestParam String typeEvent,
//                                                @NotBlank @RequestParam String startDate,
//                                                @NotBlank @RequestParam String finishDate) throws ParseException {
//
//        EventType eventType = EventType.valueOf(typeEvent.toUpperCase());
//        return analyticsEventService.getAnalytics(receiverId, eventType, startDate, finishDate);
//    }


}
