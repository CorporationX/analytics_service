package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikePostEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class LikePostEventListener extends AbstractEventListener<LikePostEventDto> {

    @Value("${spring.data.redis.channel.like-post}")
    private String likePostNotification;

    public LikePostEventListener(AnalyticsEventService analyticsEventService,
                                 ObjectMapper objectMapper,
                                 AnalyticsEventMapper analyticsEventMapper) {
        super(analyticsEventService, objectMapper, analyticsEventMapper::toAnalyticsLikePostEvent);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, LikePostEventDto.class, analyticsEventService::saveEvent);
    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(likePostNotification);
    }
}
