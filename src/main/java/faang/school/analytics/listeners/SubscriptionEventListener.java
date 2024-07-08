package faang.school.analytics.listeners;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.subscription.SubscriptionEvent;
import faang.school.analytics.service.AnalyticsEventService;

@Component
public class SubscriptionEventListener extends AbstractEventListener<SubscriptionEvent> {
    private final AnalyticsEventService eventService;
    private final AnalyticsEventMapper mapper;
    
    public SubscriptionEventListener(
        ObjectMapper objectMapper,
        RedisProperties redisProperties,
        AnalyticsEventService eventService,
        AnalyticsEventMapper mapper
    ) {
        super(objectMapper, redisProperties);
        this.eventService = eventService;
        this.mapper = mapper;
    }
    
    @Override
    public void onMessageAction(SubscriptionEvent event) {
        AnalyticsEvent model = mapper.toModel(event);
        eventService.saveEvent(model);
    }
    
    @Override
    public Class getPayloadClass() {
        return SubscriptionEvent.class;
    }
    
    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic(redisProperties.getChannels().getSubscriptionChannel());
    }
}
