package faang.school.analytics.config.messaging;

import faang.school.analytics.listener.PostViewListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPostViewConfig {

    @Value("${spring.data.redis.channel.posts-view}")
    private String channel;

    @Bean
    public MessageListenerAdapter postViewAdapter(PostViewListener postViewListener) {
        return new MessageListenerAdapter(postViewListener);
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
