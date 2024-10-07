package faang.school.analytics.subscriber;

import org.springframework.data.redis.connection.Message;

public interface MessageListener {
    void onMessage(Message message, byte[] pattern);
}
