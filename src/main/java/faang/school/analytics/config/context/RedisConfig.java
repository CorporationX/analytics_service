package faang.school.analytics.config.context;

import faang.school.analytics.listener.CommentEventListener;
import faang.school.analytics.listener.FollowerEventListener;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channels.comment-event-channel.name}")
    private String commentEventTopicName;

    @Value("${spring.data.redis.channels.follower-event-channel.name}")
    private String followerTopicName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        log.info("redis start work at port {}", port);
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Qualifier("followerListenerAdapter") MessageListenerAdapter followerListenerAdapter,
            @Qualifier("commentEventListenerAdapter") MessageListenerAdapter commentListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(followerListenerAdapter, new ChannelTopic(followerTopicName));
        container.addMessageListener(commentListenerAdapter, new ChannelTopic(commentEventTopicName));
        return container;
    }

    @Bean
    @Qualifier("followerListenerAdapter")
    public MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerTopicName);
    }

    @Bean
    @Qualifier("commentEventListenerAdapter")
    public MessageListenerAdapter CommentEventListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentEventTopicName);
    }


}