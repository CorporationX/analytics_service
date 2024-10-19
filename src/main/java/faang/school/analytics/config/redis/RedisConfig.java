package faang.school.analytics.config.redis;

import faang.school.analytics.config.redis.listener_containers.RedisContainerMessageListener;
import faang.school.analytics.redis.listener.AbstractEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import java.util.List;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
  
    @Value("${spring.data.redis.port}")
    private int port;
    
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            List<AbstractEventListener<?>> listeners) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        listeners.forEach(listener ->
                container.addMessageListener(listener, listener.getTopic())
        );

        return container;
    }

    @Bean
    public RedisMessageListenerContainer container(List<RedisContainerMessageListener> messageListeners) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        messageListeners.forEach(messageListener -> container
                .addMessageListener(messageListener.getAdapter(), messageListener.getTopic()));

        return container;
    }
    
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }
}

