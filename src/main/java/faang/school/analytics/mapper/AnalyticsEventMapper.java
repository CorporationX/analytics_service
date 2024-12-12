package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.events.PremiumBoughtEvent;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapper {
    public AnalyticsEvent map(PremiumBoughtEvent event) {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setUserId(event.getUserId());
        analyticsEvent.setAmount(event.getAmount());
        analyticsEvent.setDuration(event.getDuration());
        analyticsEvent.setTimestamp(event.getTimestamp());
        return analyticsEvent;
    }
}
