package faang.school.analytics.contoller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.BadRequestException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.EnumConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("v1/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyticsEventDto> getAnalytics(@RequestParam(name = "receiverId") long receiverId,
                                                @RequestParam(name = "eventType") String eventType,
                                                @RequestParam(name = "interval", required = false) String interval,
                                                @RequestParam(name = "from", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                                                @RequestParam(name = "to", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) throws IllegalAccessException {



        if (eventType == null || eventType.isBlank()) {
            throw new BadRequestException("Wrong event type value in the request");
        }

        if ((interval == null) && ((from == null) || (to == null))) {
            throw new BadRequestException("Wrong dates filters in the request");
        }
        return analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);
    }
}
