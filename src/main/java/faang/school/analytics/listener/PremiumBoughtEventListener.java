package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.UserPremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PremiumBoughtEventListener extends AbstractEventListener<UserPremiumBoughtEvent> {

    public PremiumBoughtEventListener(ObjectMapper objectMapper, AnalyticsService analyticsService, AnalyticsEventMapper analyticsEventMapper, Class<UserPremiumBoughtEvent> eventType) {
        super(objectMapper, analyticsService, analyticsEventMapper, eventType);
    }

    @Override
    protected AnalyticsEvent mapEvent(UserPremiumBoughtEvent event) {
        return analyticsEventMapper.toEntity(event);
    }
}
