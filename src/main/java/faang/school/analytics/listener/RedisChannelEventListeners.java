package faang.school.analytics.listener;

import org.springframework.data.redis.connection.MessageListener;

public interface RedisChannelEventListeners extends MessageListener {
    String getChannel();
}
