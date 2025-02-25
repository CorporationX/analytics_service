package faang.school.analytics.config.redis;

import faang.school.analytics.message.FollowerEvenListener;
import faang.school.analytics.message.ProjectViewEventListener;
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
                redisConfigProperties.host(), redisConfigProperties.port());
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
    public MessageListenerAdapter messageProfileViewListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(redisConfigProperties.channel().channelFollower());
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(redisConfigProperties.channel().profileView());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter followerListener,
                                                 ProjectViewEventListener projectViewEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(projectViewEventListener, profileViewTopic());
        return container;
    }
}
