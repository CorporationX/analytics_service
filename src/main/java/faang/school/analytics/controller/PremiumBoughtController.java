package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class PremiumBoughtController {
    private final AnalyticsEventService premiumBoughtService;

    @GetMapping("/analitics/{receiverId}")
    public List<AnalyticsEvent> getAnalytics(@PathVariable long receiverId, @RequestBody String type) {
        EventType eventType = EventType.valueOf(type.toUpperCase(Locale.ROOT));
        return premiumBoughtService.getAnalytics(receiverId, eventType).toList();
    }

}
