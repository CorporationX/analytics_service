package faang.school.analytics.config.context;

import faang.school.analytics.listener.comment.CommentEventListener;
import faang.school.analytics.listener.follower.FollowerEvenListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisConfigProperties redisConfigProperties;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                redisConfigProperties.getHost(), redisConfigProperties.getPort());
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter followerListener(FollowerEvenListener followerEvenListener) {
        return new MessageListenerAdapter(followerEvenListener);
    }

    @Bean
    MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(redisConfigProperties.getChannelFollower());
    }

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(redisConfigProperties.getCommentChannel());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter followerListener,
                                                 MessageListenerAdapter commentListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(commentListener, commentTopic());
        return container;
    }
}
