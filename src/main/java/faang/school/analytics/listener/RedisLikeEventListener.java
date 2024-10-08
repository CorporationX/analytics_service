package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.beans.EventHandler;

@Component
@RequiredArgsConstructor
public class RedisLikeEventListener implements MessageListener {

    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        System.out.println("You got a new like from " + channel + ": " + body);
    }
}