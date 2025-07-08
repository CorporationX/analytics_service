package faang.school.analytics.listener.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.mapper.CommentEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.CommentEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("comment-topic");
    private final RedisProperties properties;
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final CommentEventMapper commentEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEvent event = objectMapper.readValue(message.getBody(), CommentEvent.class);
            AnalyticsEvent analyticsEvent = commentEventMapper.toAnalyticsEvent(event);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Comment received event was saved, eventId: {}", analyticsEvent.getId());
        } catch (IOException e) {
            log.error("Something went wrong while converting event message to CommentEvent", e);
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(topicNameKeys, properties);
    }
}
