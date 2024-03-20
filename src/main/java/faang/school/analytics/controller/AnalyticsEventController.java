package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping("{id}")
    public AnalyticsEvent get(@PathVariable long id) {
        return analyticsEventService.getById(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        analyticsEventService.deleteEvent(id);
    }
}
