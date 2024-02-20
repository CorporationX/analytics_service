package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.service.event.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEventDto followerEventDto = objectMapper.readValue(message.getBody(), FollowerEventDto.class);
            log.info("Получено событие подписки пользователя с ID: {}, на пользователя с ID: {}",
                    followerEventDto.getFollowerId(),
                    followerEventDto.getFolloweeId());
            analyticsEventService.saveAnalyticsEvent(followerEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
