package faang.school.analytics.config.redis.listener.user;

import faang.school.analytics.service.user.listener.RedisProfileViewEventSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class UserViewContainerMessageListener {
    @Bean
    public MessageListenerAdapter profileViewEventListener(RedisProfileViewEventSubscriber messageListener) {
        return new MessageListenerAdapter(messageListener);
    }

    @Bean
    public RedisMessageListenerContainer profileViewRedisContainer(JedisConnectionFactory jedisConnectionFactory,
                                                                   ChannelTopic profileViewEventTopic,
                                                                   MessageListenerAdapter profileViewEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(profileViewEventListener, profileViewEventTopic);

        return container;
    }
}
