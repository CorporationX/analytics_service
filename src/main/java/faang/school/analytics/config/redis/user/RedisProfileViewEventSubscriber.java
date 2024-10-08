package faang.school.analytics.config.redis.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisProfileViewEventSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Received message: {}", message);
            ProfileViewEventDto profileViewEventDto = objectMapper.readValue(message.getBody(), ProfileViewEventDto.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(profileViewEventDto);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
