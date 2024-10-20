package faang.school.analytics.listener;

import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public abstract class AbstractEventListener {
    public MessageListenerAdapter createListenerAdapter() {
        return new MessageListenerAdapter(this);
    }

    public abstract String getTopic();
}
