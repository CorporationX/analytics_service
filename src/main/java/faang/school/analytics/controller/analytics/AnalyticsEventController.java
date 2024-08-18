package faang.school.analytics.controller.analytics;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import faang.school.analytics.validate.analytics.AnalyticsEventValidate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventValidate analyticsEventValidate;

    @GetMapping("/{receiverId}")
    public ResponseEntity<List<AnalyticsEventDto>> getAnalyticsByInterval(
            @PathVariable("receiverId") long receiverId,
            @RequestParam @NonNull String eventString,
            @RequestParam(name = "intervalString", required = false) String intervalString,
            @RequestParam(name = "fromDateString", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String fromDateString,
            @RequestParam(name = "toDateString", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String toDateString) {

        log.info("Received request with eventString: {}, intervalString: {}, fromDateString: {}, toDateString: {}",
                eventString, intervalString, fromDateString, toDateString);

        analyticsEventValidate.checkOfInputData(intervalString, fromDateString, toDateString);

        return ResponseEntity.status(HttpStatus.OK)
                .body(analyticsEventService.getAnalytics(receiverId, eventString, intervalString, fromDateString, toDateString));
    }
}