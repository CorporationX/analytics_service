package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.follower.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // возможно удалить
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper; // Сделтаь Json and this delete

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEventDto followerEventDto = objectMapper.readValue(message.getBody(), FollowerEventDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new String(message.getBody());
    }
}
