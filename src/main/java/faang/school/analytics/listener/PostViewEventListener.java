package faang.school.analytics.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostViewEventListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String chanel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("Received message from channel {}: {}", chanel, body);


    }
}
