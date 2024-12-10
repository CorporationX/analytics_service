package faang.school.analytics.redis.listener;

import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisListener {

    default MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

    Topic getTopic();
}
