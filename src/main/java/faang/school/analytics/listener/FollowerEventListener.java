package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.FollowerEvent;
import faang.school.analytics.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class FollowerEventListener extends AbstractListener<FollowerEvent> {
    public FollowerEventListener(ObjectMapper objectMapper, List<EventHandler<FollowerEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    protected FollowerEvent listenEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), FollowerEvent.class);
    }
}