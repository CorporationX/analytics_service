package faang.school.analytics.redis.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.follower.FollowerEvent;
import faang.school.analytics.mapper.follower.FollowerEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
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

    @Value("${spring.data.redis.channel.follower-event-channel}")
    private String followerEventChannel;

    private final FollowerEventMapper followerEventMapper;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventService analyticsEventService,
                                 FollowerEventMapper followerEventMapper) {
        super(objectMapper, analyticsEventService);
        this.followerEventMapper = followerEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent followerEvent = mapMessage(message, FollowerEvent.class);
        AnalyticsEvent analyticsEvent = followerEventMapper.toAnalyticsEvent(followerEvent);
        analyticsEvent.setEventType(EventType.FOLLOWER);
        save(analyticsEvent);
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(followerEventChannel);
    }
}