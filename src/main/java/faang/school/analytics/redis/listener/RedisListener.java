package faang.school.analytics.redis.listener;

import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisListener {

    MessageListenerAdapter getAdapter();

    Topic getTopic();
}
