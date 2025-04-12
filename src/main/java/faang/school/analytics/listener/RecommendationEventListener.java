package faang.school.analytics.listener;

import faang.school.analytics.dto.event.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendationEventListener {
    private final AnalyticsEventService analyticsEventService;


    public void handleMessage(RecommendationEvent event) {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
    }
}
