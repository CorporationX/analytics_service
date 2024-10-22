package faang.school.analytics.config.redis;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

public interface EventRedisConfig {

    ChannelTopic getTopic();

    MessageListener getAdapter();
}
