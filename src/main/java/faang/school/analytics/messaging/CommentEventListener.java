package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.CommentEventMapper;
import faang.school.analytics.model.CommentEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final CommentEventMapper commentEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), CommentEvent.class);
        } catch (IOException e) {
            log.error("Something went wrong while converting event message to CommentEvent", e);
            throw new RuntimeException(e.getMessage());// не могу понять, куда полетит эта ошибка, не знаю, какой класс применить
        }
        analyticsEventService.saveEvent(commentEventMapper.toAnalyticsEvent(event));
    }
}
