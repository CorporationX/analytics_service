package faang.school.analytics.config.redis;

import faang.school.analytics.services.redis.PostViewedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channel.post_view_event_channel}")
    private String postViewEventChannel;

    @Bean
    public MessageListenerAdapter messageListenerAdapter(PostViewedEventListener postViewedEventListener) {
        return new MessageListenerAdapter(postViewedEventListener);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    ChannelTopic postViewChannel() {
        return new ChannelTopic(postViewEventChannel);
    }

    @Bean
    public RedisMessageListenerContainer container(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListenerAdapter, postViewChannel());
        return container;
    }
}

