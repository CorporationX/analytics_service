package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisListener;
import faang.school.analytics.dto.subscription.FollowerEventDto;
import faang.school.analytics.model.EventType;
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
    private final AnalyticsEventSaver analyticsEventSaver;

    public FollowerEventListener(ObjectMapper objectMapper, AnalyticsEventSaver analyticsEventSaver) {
        super(objectMapper, FollowerEventDto.class);
        this.analyticsEventSaver = analyticsEventSaver;
    }

    @Override
    public void handleEvent(FollowerEventDto event) {
        log.info("Received follower event: {}", event);
        analyticsEventSaver.saveAnalyticsEvent(event, EventType.FOLLOWER);
    }
}