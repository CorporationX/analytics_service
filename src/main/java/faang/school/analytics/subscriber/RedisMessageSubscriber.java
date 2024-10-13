package faang.school.analytics.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RedisMessageSubscriber implements MessageListener {
    public static List<String> messageList;

    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        log.info("recieved message: {}", message);
    }
}
