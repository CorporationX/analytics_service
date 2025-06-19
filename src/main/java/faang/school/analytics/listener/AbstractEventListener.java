package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final AnalyticsEventService analyticsEventService;
    protected final ObjectMapper objectMapper;
    protected final RedisProperties redisProperties;

    protected AbstractEventListener(AnalyticsEventService analyticsEventService,
                                    ObjectMapper objectMapper,
                                    RedisProperties redisProperties) {
        this.analyticsEventService = analyticsEventService;
        this.objectMapper = objectMapper;
        this.redisProperties = redisProperties;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("{} received a message:\n{}", getClass().getSimpleName(), message.getBody());
        try {
            T event = convertMessage(message);
            log.info("Message was successfully converted to {}:\n{}",
                    event.getClass().getSimpleName(), event);

            AnalyticsEvent analyticsEvent = mapToAnalyticsEvent(event);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            log.warn("Failed to process message. Message body:\n{}", message.getBody());
            throw new RuntimeException(e);
        }
    }

    public Set<ChannelTopic> getChannelTopics() {
        return getChanelTopics(getTopicNameKeys(), redisProperties);
    }

    protected abstract List<String> getTopicNameKeys();

    protected abstract Class<T> getEventClass();

    protected abstract AnalyticsEvent mapToAnalyticsEvent(T event);

    private T convertMessage(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), getEventClass());
    }

    private Set<ChannelTopic> getChanelTopics(List<String> topicNames, RedisProperties properties) {
        return properties.getChannels().entrySet().stream()
                .filter(entry -> topicNames.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(ChannelTopic::new)
                .collect(Collectors.toSet());
    }
}
