package faang.school.analytics.listeners;

import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Интерфейс с методом для получения топика из Listenerа
 */
public interface TopicProvider {
    ChannelTopic getTopic();
}
