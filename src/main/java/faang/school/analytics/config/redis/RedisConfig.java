package faang.school.analytics.config.redis;

import faang.school.analytics.listener.CommentEventListener;
import faang.school.analytics.publish.listener.like.PostLikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }


    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                  MessageListenerAdapter messageListenerAdapter,
                                                                  MessageListenerAdapter commentMessageListener
        ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, postLikeEventTopic());
        container.addMessageListener(commentMessageListener, commentEventTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(PostLikeEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter commentMessageListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }


    @Bean
    public ChannelTopic postLikeEventTopic() {
        return new ChannelTopic(redisProperties.getPostLikeEventChannelName());
    }
    @Bean
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(redisProperties.getCommentEventChannelName());
    }
}
