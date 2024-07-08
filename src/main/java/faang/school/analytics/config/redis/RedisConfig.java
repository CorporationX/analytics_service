package faang.school.analytics.config.redis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import faang.school.analytics.listeners.AbstractEventListener;
import faang.school.analytics.listeners.TopicProvider;

@Configuration
public class RedisConfig {
    @Bean
    public JedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        return new JedisConnectionFactory(config);
    }
    
    @Bean
    RedisMessageListenerContainer redisContainer(
        RedisConnectionFactory redisConnectionFactory,
        @Qualifier("listenersMap") Map<ChannelTopic, MessageListenerAdapter> listeners
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        //Заполняется автоматически сколько бы реализаций Listener не было
        listeners.forEach((topic, listener) -> container.addMessageListener(listener, topic));
        
        return container;
    }
    
    /**
     * Создаем Map с ключом - название топика, который слушает Listener, а значением - самим Listener
     */
    @Bean("listenersMap")
    Map<ChannelTopic, MessageListenerAdapter> listenersMap(List<AbstractEventListener> abstractEventListenerList) {
        return abstractEventListenerList.stream().collect(Collectors.toMap(
            TopicProvider::getTopic,
            MessageListenerAdapter::new)
        );
    }
}