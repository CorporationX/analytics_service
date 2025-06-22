package faang.school.analytics.event_listner;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageConsumer implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
