package faang.school.analytics.controller.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.service.event.EventParamService;
import faang.school.analytics.service.event.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@Validated
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final EventParamService eventParamService;
    @PostMapping()
    public EventDto addNewEvent(@Valid @RequestBody EventDto eventDto) {
        return analyticsEventService.addNewEvent(eventDto);
    }

    @GetMapping("/get")
    public List<EventDto> getEvents(
            @RequestParam long receiverId,
            @RequestParam String eventType,
            @RequestParam(required = false) String interval,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        return eventParamService.getEventsDto(receiverId, eventType, interval, from, to);
    }


}
