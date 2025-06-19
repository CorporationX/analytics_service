package faang.school.analytics.config.redis;

import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.PostViewEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.postView}")
    private String postViewChannel;

    @Value("${spring.data.redis.channel.likesReceived}")
    private String likesChannel;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            @Qualifier("listenerLikesEventAdapter") MessageListenerAdapter listenerLikesEventAdapter,
            @Qualifier("likesTopic") ChannelTopic likesTopic,
            @Qualifier("listenerPostViewEventAdapter") MessageListenerAdapter listenerPostViewEventAdapter,
            @Qualifier("postViewTopic") ChannelTopic postViewTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerLikesEventAdapter, likesTopic);
        container.addMessageListener(listenerPostViewEventAdapter, postViewTopic);
        return container;
    }

    @Bean
    @Qualifier("listenerPostViewEventAdapter")
    public MessageListenerAdapter listenerPostViewChannelAdapter(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    @Qualifier("listenerLikesEventAdapter")
    public MessageListenerAdapter listenerLikesEventAdapter(LikeEventListener likesEventListener) {
        return new MessageListenerAdapter(likesEventListener);
    }

    @Qualifier("postViewTopic")
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewChannel);
    }

    @Bean
    @Qualifier("likesTopic")
    public ChannelTopic likesTopic() {
        return new ChannelTopic(likesChannel);
    }
}