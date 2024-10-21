package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PremiumBoughtEventListener extends AbstractEventListenerList<PremiumBoughtEventDto> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public PremiumBoughtEventListener(ObjectMapper javaTimeModuleObjectMapper,
                                      Topic premiumBoughtEventTopic,
                                      AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper analyticsEventMapper) {
        super(javaTimeModuleObjectMapper, premiumBoughtEventTopic);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void saveEvent(PremiumBoughtEventDto event) {
        analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEvent(event));
    }

    @Override
    public Class<PremiumBoughtEventDto> getEventType() {
        return PremiumBoughtEventDto.class;
    }

    @Override
    protected String getEventTypeName() {
        return "Premium bought events";
    }
}
