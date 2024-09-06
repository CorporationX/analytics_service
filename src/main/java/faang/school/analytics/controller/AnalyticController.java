package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventRequestDto;
import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.exception.handler.BadRequestException;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author Evgenii Malkov
 */
@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticController {

    public final AnalyticsEventService analyticsEventService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<AnalyticsEventDto> getAnalyticsEvent(AnalyticsFilterDto analyticsFilterDto) {

        return analyticsEventService.getAnalytics(analyticsFilterDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Validated AnalyticsEventDto analyticsEventDto) {
        analyticsEventService.saveEvent(analyticsEventDto);
    }
}
