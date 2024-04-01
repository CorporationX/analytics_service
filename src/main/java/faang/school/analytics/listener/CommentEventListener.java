package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.handler.EventHandler;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> implements MessageListener {
    public CommentEventListener(ObjectMapper objectMapper, List<EventHandler<CommentEventDto>> eventHandlers, AnalyticsEventService analyticsEventService) {
        super(objectMapper, eventHandlers, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEventForAllHandlers(message, CommentEventDto.class);
    }

}
