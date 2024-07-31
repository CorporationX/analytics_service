package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
public class CommentEventListener extends AbstractListener<CommentEvent> {


    public CommentEventListener(ObjectMapper objectMapper, List<EventHandler<CommentEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    protected CommentEvent listenEvent(Message message) throws IOException {
        String jsonMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        return objectMapper.readValue(jsonMessage, CommentEvent.class);
    }
}

