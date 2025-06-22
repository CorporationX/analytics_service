package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsEventService service;

    @GetMapping("/events")
    public Iterable<AnalyticsEvent> getAll() {
        return service.findAll(); // добавь метод в сервис
    }

    @GetMapping("/events/recipient/{recipientId}")
    public List<AnalyticsEvent> getByRecipient(@PathVariable Long recipientId) {
        return service.findByRecipientId(recipientId);
    }

}
