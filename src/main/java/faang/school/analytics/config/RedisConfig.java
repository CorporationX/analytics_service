package faang.school.analytics.config;

import faang.school.analytics.messaging.CommentEventListener;
import faang.school.analytics.messaging.FundRaisedEventListener;
import faang.school.analytics.messaging.followEvent.FollowEventListener;
import lombok.RequiredArgsConstructor;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.follower_channel.name}")
    private String followerTopic;
    @Value("${spring.data.redis.channels.comment_channel.name}")
    private String commentEvent;
    @Value("${spring.data.redis.channels.fund_raised_event.name}")
    private String fundRaisedEvent;
    private final FollowEventListener followEventListener;
    private final CommentEventListener commentEventListener;
    private final FundRaisedEventListener fundRaisedEventListener;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        System.out.println(port);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerTopic);
    }

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(commentEvent);
    }

    @Bean
    ChannelTopic fundRaisedTopic() {
        return new ChannelTopic(fundRaisedEvent);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(new MessageListenerAdapter(followEventListener), followerTopic());
        container.addMessageListener(new MessageListenerAdapter(commentEventListener), commentTopic());
        container.addMessageListener(new MessageListenerAdapter(followEventListener), fundRaisedTopic());
        return container;
    }
}
