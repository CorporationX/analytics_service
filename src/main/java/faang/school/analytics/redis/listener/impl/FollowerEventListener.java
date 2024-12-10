package faang.school.analytics.redis.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.follower.FollowerEvent;
import faang.school.analytics.mapper.follower.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.listener.AbstractEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    @Value("${spring.data.redis.channel.follower-event}")
    private String followerEventChannel;

    private final AnalyticsEventMapper analyticsEventMapper;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent followerEvent = mapMessage(message, FollowerEvent.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.followerEventToAnalyticsEvent(followerEvent);
        save(analyticsEvent);
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(followerEventChannel);
    }
}