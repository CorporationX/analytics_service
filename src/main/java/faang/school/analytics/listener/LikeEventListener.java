package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;


    public void onMessage(Message message, byte[] pattern) {
        log.info("Message received: {}", message.toString());
        try {
            LikeEvent event = objectMapper.readValue(message.getBody(), LikeEvent.class);
            analyticsEventService.saveLikeEvent(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
