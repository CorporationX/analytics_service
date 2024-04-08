package faang.school.analytics.service;

import faang.school.analytics.listener.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final RedisMessagePublisher redisPublisher;

    public void publishProjectViewEvent(Long userId, Long viewedProfileId, LocalDateTime timestamp) {
        String jsonMessage = createJsonMessage(userId, viewedProfileId, timestamp);

        redisPublisher.publish(jsonMessage);
    }

    private String createJsonMessage(Long userId, Long viewedProfileId, LocalDateTime timestamp) {
        return null;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("view-project");
    }
}
