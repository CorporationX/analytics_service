package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Этот контроллер создан только для тестирования корректности работы
 * @see AnalyticsEventService и его методов
 * @see AnalyticsEventService#saveEvent(AnalyticsEventDto eventDto) - Этот метод сохраняет переданный объект в БД
 //* @see AnalyticsEventService#getAnalytics(AnalyticsRequest request) - Данный метод достает из БД все объекты AnalyticsEvent с данным receiverId и eventType.
 */

@RestController
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @PostMapping("/analytics")
    public void saveEvent(@RequestBody AnalyticsEventDto analyticsEventDto) {
        analyticsEventService.saveEvent(analyticsEventDto);
    }

    @GetMapping("/analytics")
    public ResponseEntity<List<AnalyticsEventDto>> getAnalytics(@RequestParam long receiverId,
                                                               @RequestParam String eventType,
                                                               @RequestParam(required = false) String interval,
                                                               @RequestParam(required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                               @RequestParam(required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to) {
        
        return ResponseEntity.ok(analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to));
    }

}
