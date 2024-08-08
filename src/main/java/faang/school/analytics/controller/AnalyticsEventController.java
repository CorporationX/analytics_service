package faang.school.analytics.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.convertor.EnumConvertor;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.Interval;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Validated
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    @GetMapping
    public List<AnalyticsEventDto> getAnalyticsEvents(@RequestParam @NotNull @Positive long receiverId,
                                                      @RequestParam String eventType,
                                                      @RequestParam(required = false) String interval,
                                                      @RequestParam(required = false)
                                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime from,
                                                      @RequestParam(required = false)
                                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime to
                                                      ) throws IllegalAccessException {

        EventType eventTypeName = EnumConvertor.convert(EventType.class, eventType);
        Interval intervalName = EnumConvertor.convert(Interval.class, interval);

        return analyticsEventService.getAnalytics(receiverId, eventTypeName, intervalName, from, to);
    }
}
