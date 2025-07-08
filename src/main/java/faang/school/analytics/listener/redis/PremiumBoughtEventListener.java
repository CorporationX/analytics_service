package faang.school.analytics.listener.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.premium.PremiumBoughtEvent;
import faang.school.analytics.mapper.event.UserServiceEventMapper;
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
public class PremiumBoughtEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("premium-bought");
    private final RedisProperties properties;
    private final ObjectMapper objectMapper;
    private final UserServiceEventMapper userServiceEventMapper;
    private final AnalyticsEventService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.debug("Received premium bought event");
            PremiumBoughtEvent event = objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class);
            service.saveEvent(userServiceEventMapper.premiumBoughtToAnalytics(event));
            log.info("Premium bought event for user with id {} analytics successfully saved", event.userId());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(topicNameKeys, properties);
    }
}