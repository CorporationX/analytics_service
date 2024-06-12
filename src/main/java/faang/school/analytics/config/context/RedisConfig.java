package faang.school.analytics.config.context;

import faang.school.analytics.listner.PostViewEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.post-view-channel}")
    private String postViewChannelName;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter postViewListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        container.addMessageListener(postViewListener, postViewTopic());

        return container;
    }

    @Bean
    public MessageListenerAdapter postViewListener(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewChannelName);
    }
}
