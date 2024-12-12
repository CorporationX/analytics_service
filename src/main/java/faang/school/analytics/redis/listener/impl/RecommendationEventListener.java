package faang.school.analytics.redis.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.RecommendationEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.redis.listener.AbstractEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> {

    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    private final AnalyticsEventMapper analyticsEventMapper;

    public RecommendationEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper
    ) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationEvent recommendationEvent = mapMessage(message, RecommendationEvent.class);
        save(analyticsEventMapper.recommendationEventToAnalyticsEvent(recommendationEvent));
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(recommendationChannel);
    }
}
