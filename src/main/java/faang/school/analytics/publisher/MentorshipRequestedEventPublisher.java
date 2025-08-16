package faang.school.analytics.publisher;

import faang.school.analytics.analytics_event.MentorshipRequestedEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestedEventPublisher {

    private static final String CHANNEL = "mentorship.requested";

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(MentorshipRequestedEvents event) {
        log.info("Отправляю событие: {}", event);
        redisTemplate.convertAndSend(CHANNEL, event);
    }
}