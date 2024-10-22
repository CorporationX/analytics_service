package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEventDto> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public PremiumBoughtEventListener(ObjectMapper objectMapper,
                                      AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper analyticsEventMapper,
                                      @Value("${spring.data.redis.channel.premium_bought}") String premiumBoughtEventTopic) {
        super(objectMapper, new ChannelTopic(premiumBoughtEventTopic));
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

}
