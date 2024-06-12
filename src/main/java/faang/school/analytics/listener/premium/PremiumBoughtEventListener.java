package faang.school.analytics.listener.premium;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.premium.PremiumBoughtEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.premium.PremiumBoughtEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> {

    private final PremiumBoughtEventMapper premiumBoughtEventMapper;
    private final AnalyticsService analyticsService;

    public PremiumBoughtEventListener(ObjectMapper objectMapper, PremiumBoughtEventMapper premiumBoughtEventMapper, AnalyticsService analyticsService) {
        super(objectMapper);
        this.premiumBoughtEventMapper = premiumBoughtEventMapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        System.out.println();
        System.out.println(message);
        System.out.println();
        handleEvent(message, PremiumBoughtEvent.class, event -> {
            AnalyticsEvent analyticsEvent = premiumBoughtEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.PREMIUM_BOUGHT_EVENT);
            analyticsService.save(analyticsEvent);
        });
    }
}
