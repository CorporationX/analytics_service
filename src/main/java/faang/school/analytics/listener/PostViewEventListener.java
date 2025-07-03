package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.post.PostViewEvent;
import faang.school.analytics.mapper.event.UserServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostViewEventListener extends AbstractEventListener {
    private static final String POST_VIEW_TOPIC_NAME_KEY = "post-view";

    private final RedisProperties properties;
    private final ObjectMapper objectMapper;
    private final UserServiceEventMapper eventMapper;
    private final AnalyticsEventService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Post view event received: {}", message.toString());
        try {
            PostViewEvent eventDto = objectMapper.readValue(message.getBody(), PostViewEvent.class);
            AnalyticsEvent event = eventMapper.postViewToAnalytics(eventDto);
            service.saveEvent(event);
            log.info("Post view [{}] event was saved, eventId [{}]", message, event.getId());
        } catch (Exception e) {
            log.error("Error on processing post view event \"{}\": {}", message.toString(), e.getMessage(), e);
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(List.of(POST_VIEW_TOPIC_NAME_KEY), properties);
    }
}
