package faang.school.analytics.config.redis;

import faang.school.analytics.redis.listener.AbstractEventListener;
import faang.school.analytics.redis.listener.AbstractListEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            List<AbstractEventListener<?>> eventListeners,
            List<AbstractListEventListener<?>> listEventListeners) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        eventListeners.forEach(listener ->
                container.addMessageListener(listener, listener.getTopic())
        );

        listEventListeners.forEach(listener ->
                container.addMessageListener(listener, listener.getTopic())
        );

        return container;
    }
}
