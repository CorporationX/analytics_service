package faang.school.analytics.config.redis.user;

import faang.school.analytics.service.user.listener.RedisProfileViewEventSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class UserRedisListenerConfig {
    @Value("${app.user-redis-config.profile_view_event_topic}")
    private String profileViewEventTopic;

    @Bean
    @Qualifier("profileViewEventTopic")
    public ChannelTopic profileViewEventTopic() {
        return new ChannelTopic(profileViewEventTopic);
    }

    @Bean
    @Qualifier("profileViewEventListener")
    public MessageListenerAdapter profileViewEventListener(RedisProfileViewEventSubscriber messageListener) {
        return new MessageListenerAdapter(messageListener);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(JedisConnectionFactory jedisConnectionFactory,
                                                        @Qualifier("profileViewEventTopic")
                                                        ChannelTopic topic,
                                                        @Qualifier("profileViewEventListener")
                                                        MessageListenerAdapter listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(listener, topic);

        return container;
    }
}
