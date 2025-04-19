package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static faang.school.analytics.constants.Constants.DATE_FORMAT;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final List<DateTimeFormatter> formatters = List.of(DateTimeFormatter.ofPattern(DATE_FORMAT),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    @GetMapping
    public ResponseEntity<List<AnalyticsEvent>> getAnalytics(@RequestParam long receiverId,
                                                             @RequestParam String eventType,
                                                             @RequestParam(required = false) String interval,
                                                             @RequestParam(required = false) String startDate,
                                                             @RequestParam(required = false) String endDate) {

        log.info("Received analytics request for receiverId={}, eventType={}, interval={}, startDate={}, endDate={}",
                receiverId, eventType, interval, startDate, endDate);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, startDate,
                endDate);

        log.info("Analytics data returned: {} events", result.size());

        return ResponseEntity.ok(result);
    }

}

