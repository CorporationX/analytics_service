package faang.school.analytics.config.redis.eventconfig;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public abstract class AbstractEventRedisConfig implements EventRedisConfig {

    protected final ChannelTopic topic;
    protected final MessageListenerAdapter adapter;

    public AbstractEventRedisConfig(
        String topicName,
        MessageListener eventListener
    ) {
        this.topic = new ChannelTopic(topicName);
        this.adapter =  new MessageListenerAdapter(eventListener);
    }

    @Override
    public abstract ChannelTopic getTopic();

    @Override
    public abstract MessageListener getAdapter();
}
