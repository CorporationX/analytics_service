package faang.school.analytics.controller.events;

import faang.school.analytics.domain.dto.events.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.AnalyticsEventFilterDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.service.events.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/analytics/events")
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public AnalyticsEventDto saveEvent(@Valid @RequestBody AnalyticsEventDto event) {
        if (event.getId() != null) {
            log.warn("Attempt save event with id");
            throw new DataValidationException("Event don't have id for save");
        }
        log.info("Save event. Type = {}. ReceiverId = {}. ActorId = {}.", event.getEventType(), event.getReceivedAt(), event.getActorId());
        return analyticsEventService.saveEvent(event);
    }

    @GetMapping
    public List<AnalyticsEventDto> getEvents(@ModelAttribute @Valid AnalyticsEventFilterDto filterDto) {
        if (filterDto.getInterval() != null && (filterDto.getFrom() != null || filterDto.getTo() != null)) {
            log.warn("Incorrect filter for get events: interval = {}, fromAt = {}, toAt = {}", filterDto.getInterval(), filterDto.getFrom(), filterDto.getTo());
            throw new DataValidationException("Search filter required 'Interval' or 'Dates'");
        }

        log.info("Requested events with filter: interval = {}, fromAt = {}, toAt = {}",
                filterDto.getInterval(), filterDto.getFrom(), filterDto.getTo());
        return analyticsEventService.getAnalytics(filterDto);
    }
}
