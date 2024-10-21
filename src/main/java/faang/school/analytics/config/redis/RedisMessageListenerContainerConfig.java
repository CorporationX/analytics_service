package faang.school.analytics.config.redis;

import faang.school.analytics.config.redis.listener_containers.RedisContainerMessageListener;
import faang.school.analytics.redis.listener.AbstractEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

@Configuration
public class RedisMessageListenerContainerConfig {
    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        List<AbstractEventListener<?>> listeners,
                                                        List<RedisContainerMessageListener> messageListeners) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        listeners.forEach(listener -> container.addMessageListener(listener, listener.getTopic()));
        messageListeners.forEach(listener -> container.addMessageListener(listener.getAdapter(), listener.getTopic()));

        return container;
    }
}
