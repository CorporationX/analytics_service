package faang.school.analytics.config.redis.listener_containers;

import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisContainerMessageListener {
    MessageListenerAdapter getAdapter();

    Topic getTopic();
}
