package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisListener;
import faang.school.analytics.dto.subscription.FollowerEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Слушатель событий подписки пользователей.
 * <p>
 * Получает события о подписках пользователей из Redis и сохраняет их в базе данных.
 * </p>
 */
@Slf4j
@Component
@RedisListener(topic = "follower_event")
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> {
    public FollowerEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService, FollowerEventDto.class);
    }

    @Override
    public void handleEvent(FollowerEventDto event) {
        log.info("Received follower event: {}", event);
        saveAnalyticsEvent(event.getFollowerId(), event.getFolloweeId(), EventType.FOLLOWER);
    }
}