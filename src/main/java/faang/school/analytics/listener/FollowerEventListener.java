package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
    }
}
