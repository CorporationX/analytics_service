package faang.school.analytics.controller;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventRequestDto;
import faang.school.analytics.dto.interval.IntervalDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/analytics")
public class AnalyticsEventV1Controller {
    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public AnalyticsEventDto createEvent(@RequestBody @Valid AnalyticsEventRequestDto eventDto,
                                         @RequestParam @NotNull EventType eventType) {
        return analyticsEventService.createEvent(eventDto, eventType);
    }

    @GetMapping("{receiverId}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable @Positive long receiverId,
                                                @RequestParam @NotNull EventType eventType,
                                                @RequestBody(required = false) @Valid IntervalDto interval,
                                                @RequestParam(required = false) @PastOrPresent LocalDateTime from,
                                                @RequestParam(required = false) LocalDateTime to) {
        return analyticsEventService.getAnalytics(receiverId, interval, eventType, from, to);
    }

    @DeleteMapping("{eventId}")
    public void deleteEvent(@PathVariable @Positive long eventId) {
        analyticsEventService.deleteEvent(eventId);
    }

}
