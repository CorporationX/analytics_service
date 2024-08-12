package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventRequestDto;
import faang.school.analytics.exception.handler.BadRequestException;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Evgenii Malkov
 */
@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticController {

    public final AnalyticsEventService analyticsEventService;

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@ModelAttribute @Validated AnalyticsEventRequestDto request) {
        if (request.getInterval() == null && (request.getFrom() == null || request.getTo() == null)) {
            throw new BadRequestException("No period filter value specified");
        }
        return analyticsEventService.getAnalytics(request.getId(), request.getEventType(),
                request.getInterval(), request.getFrom(), request.getTo());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Validated AnalyticsEventDto analyticsEventDto) {
        analyticsEventService.saveEvent(analyticsEventDto);
    }
}
