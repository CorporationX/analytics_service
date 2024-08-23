package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.CommentEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final CommentEventMapper commentEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            var commentEvent = objectMapper.readValue(message.getBody(), CommentEvent.class);
            var analyticsEventDto = commentEventMapper.toAnalyticsEventDto(commentEvent);
            analyticsEventService.saveEvent(analyticsEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
