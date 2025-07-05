package faang.school.analytics.listener.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.event.PostServiceEventMapper;
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
public class LikeEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("like-event");
    private final AnalyticsEventService service;
    private final ObjectMapper objectMapper;
    private final PostServiceEventMapper postServiceEventMapper;
    private final RedisProperties properties;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEvent event = objectMapper.readValue(message.getBody(), LikeEvent.class);
            service.saveEvent(postServiceEventMapper.likeEventToAnalytics(event));
            log.debug("Like event was saved, for post with ID: {}", event.postId());
        } catch (IOException e) {
            log.warn("Failed to process message. Message body:\n{}", message.getBody());
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(topicNameKeys, properties);
    }
}
