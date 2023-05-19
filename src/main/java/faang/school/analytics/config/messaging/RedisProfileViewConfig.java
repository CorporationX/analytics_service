package faang.school.analytics.config.messaging;

import faang.school.analytics.messaging.UserProfileViewListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisProfileViewConfig {

    @Value("${spring.data.redis.channel.profile-view}")
    private String channel;

    @Bean
    public MessageListenerAdapter profileViewAdapter(UserProfileViewListener profileViewListener) {
        return new MessageListenerAdapter(profileViewListener);
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(channel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(JedisConnectionFactory connectionFactory,
                                                        MessageListenerAdapter profileViewAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(profileViewAdapter, topic());
        return container;
    }
}
