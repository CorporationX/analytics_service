package faang.school.analytics.service.redis.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@Slf4j
public abstract class BaseListener implements MessageListener {
    public abstract void listen(Message message, byte[] pattern) throws IOException;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            listen(message, pattern);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
