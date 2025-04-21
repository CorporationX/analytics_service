package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.exception.AnalyticsConvertingException;
import faang.school.analytics.mapper.FollowerEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsService;
    private final FollowerEventMapper followerEventMapper;


    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            analyticsService.saveEvent(followerEventMapper.toAnalyticsDto(event));
            log.info("Получено новое событие, пользователь {} подписался на пользователя {}",
                    event.followerId(), event.followeeId());
        } catch (IOException e) {
            throw new AnalyticsConvertingException("Ошибка чтения JSON", e);
        }
    }
}
