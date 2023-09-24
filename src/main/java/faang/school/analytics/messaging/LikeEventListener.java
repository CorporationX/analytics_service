package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.service.redis.events.LikeEvent;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    @Autowired
    public LikeEventListener(ObjectMapper objectMapper) {
        super(objectMapper);
    }


    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        LikeEvent likeEvent = mapEvent(message, LikeEvent.class);
    }
}
