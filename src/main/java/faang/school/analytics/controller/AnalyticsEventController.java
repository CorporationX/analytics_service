package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService service;

    @GetMapping("/{receiverId}/events")
    public ResponseEntity<List<AnalyticsEventDto>> getAnalyticsEvents(
            @PathVariable long receiverId,
            @RequestParam(name = "eventType") String eventType) {

        // Здесь вам также может потребоваться преобразование eventType в EventType
        // Например: EventType eventTypeEnum = EventType.valueOf(eventType);

        List<AnalyticsEventDto> analyticsEvents = service.getAnalytics(receiverId, eventTypeEnum);
        return ResponseEntity.ok(analyticsEvents);
    }
}
