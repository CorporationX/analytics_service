package faang.school.analytics.config.reddis;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public interface RequesterRedis<E> {
    default Pair<MessageListenerAdapter, ChannelTopic> getRequester() {
        return Pair.of(
                new MessageListenerAdapter(getMethodListener()),
                new ChannelTopic(getChannel()));
    }

    String getChannel();

    E getMethodListener();
}