package faang.school.analytics.controller;

import faang.school.analytics.convertor.EnumConvertor;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.filters.timeManagment.TimeRange;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
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
    @Value("${spring.data.time_format}")
    private String timeFormat;

    @GetMapping
    public List<AnalyticsEventDto> getAnalyticsEvents(@RequestParam @NotNull @Positive long receiverId,
                                                      @RequestParam String eventType,
                                                      @RequestParam(required = false) String interval,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(pattern = "timeFormat") LocalDateTime from,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(pattern = "timeFormat") LocalDateTime to
    ) {

        EventType eventTypeName = EnumConvertor.convert(EventType.class, eventType);
        Interval intervalName = EnumConvertor.convert(Interval.class, interval);

        TimeRange timeRange = new TimeRange();
        timeRange.setStart(from);
        timeRange.setEnd(to);
        validateTimeRange(intervalName, timeRange);
        return analyticsEventService.getAnalytics(receiverId, eventTypeName, intervalName, from, to);
    }

    private void validateTimeRange(Interval interval, TimeRange timeRange) {
        if (interval == null && timeRange.isEmpty()) {
            throw new IllegalArgumentException("You must specify one of intervals");
        } else if (interval == null && !timeRange.isCompletelyFilled()) {
            throw new IllegalArgumentException("Custom interval must be completely filled");
        } else if (interval != null && timeRange.isCompletelyFilled()) {
            throw new IllegalArgumentException("Not allowed to specify both intervals");
        }
    }
}
