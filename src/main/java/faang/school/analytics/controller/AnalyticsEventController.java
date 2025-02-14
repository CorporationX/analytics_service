package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventRequestDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/analytics")
@Slf4j
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public void saveEvent(@RequestBody AnalyticsEventDto event) {
        checkDataBeforeSave(event);
        analyticsEventService.saveEvent(event);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(@RequestBody AnalyticsEventRequestDto analyticsEventRequestDto) {
        return analyticsEventService.getAnalytics(analyticsEventRequestDto);
    }

    private void checkDataBeforeSave(AnalyticsEventDto event) {
        if (event == null) {
            log.error("Analytics event can't be null.");
            throw new DataValidationException("Analytics event can't be null.");
        }
        if (event.getEventType() == null) {
            log.error("Event type can't be null.");
            throw new DataValidationException("Event type can't be null.");
        }
        if (event.getActorId() == 0) {
            log.error("Actor id can't be 0.");
            throw new DataValidationException("Actor id can't be 0.");
        }
        if (event.getReceiverId() == 0) {
            log.error("Receiver id can't be 0.");
            throw new DataValidationException("Receiver id can't be 0.");
        }
    }
}
