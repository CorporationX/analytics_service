package faang.school.analytics.listener.following;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.following.FollowerEvent;
import faang.school.analytics.mapper.following.FollowerEventMapper;
import faang.school.analytics.service.events.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final FollowerEventMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            analyticsEventService.saveEvent(mapper.toCommonEvent(event));
        } catch (IOException e) {
            log.error("Error reading value from topic {}", message.getChannel());
            throw new RuntimeException(e);
        }

    }
}
