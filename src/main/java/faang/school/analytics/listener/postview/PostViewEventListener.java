package faang.school.analytics.listener.postview;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.postview.PostViewEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostViewEvent postViewEvent = objectMapper.readValue(message.getBody(), PostViewEvent.class);
            analyticsService.savePostViewEvent(postViewEvent);
        } catch (IOException e) {
            log.error("IOException was thrown", e);
            throw new SerializationException("Failed to deserialize message", e);
        }
    }
}
