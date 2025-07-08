package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.mapper.event.UserServiceEventMapper;
import faang.school.analytics.model.FollowerEvent;
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
public class FollowerEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("follower-event");
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final UserServiceEventMapper userServiceEventMapper;
    private final RedisProperties properties;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent followerEvent = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            analyticsEventService.saveEvent(userServiceEventMapper.followerToAnalytics(followerEvent));
            log.info("Follower Event was saved with followerID: {}, for foloweeId: {}",
                    followerEvent.getFollowerId(), followerEvent.getFolloweeId());
        } catch (IOException e) {
            log.error("Convert message to Follower event and save was failed", e);
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return getChannelTopics(topicNameKeys, properties);
    }
}