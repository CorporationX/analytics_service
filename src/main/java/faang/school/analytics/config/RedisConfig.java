package faang.school.analytics.config;

import faang.school.analytics.listener.CommentEventListener;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.PremiumEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.channels.follower_channel.name}")
    private String followerChannelName;
    @Value("${spring.data.redis.channels.premium_channel.name}")
    private String premiumChannelName;
    @Value("${spring.data.redis.channels.comment_channel.name}")
    private String commentChannelName;

    @Value("${spring.data.redis.channels.goal_completed_channel.name}")
    private String goalCompletedChannelName;

    private final FollowerEventListener followerEventListener;
    private final PremiumEventListener premiumEventListener;
    private final CommentEventListener commentEventListener;

    private final GoalCompletedEventListener goalCompletedEventListener;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    ChannelTopic followerChannel() {
        return new ChannelTopic(followerChannelName);
    }

    @Bean
    ChannelTopic premiumChannel() {
        return new ChannelTopic(premiumChannelName);
    }

    @Bean
    ChannelTopic goalCompletedChannel() {
        return new ChannelTopic(goalCompletedChannelName);
    }

    @Bean
    ChannelTopic commentChannel() {
        return new ChannelTopic(commentChannelName);
    }

    @Bean
    MessageListenerAdapter followerListener() {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter premiumListener() {
        return new MessageListenerAdapter(premiumEventListener);
    }

    @Bean
    MessageListenerAdapter goalCompletedListener() {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    MessageListenerAdapter commentListener() {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener(), followerChannel());
        container.addMessageListener(premiumListener(), premiumChannel());
        container.addMessageListener(goalCompletedListener(), goalCompletedChannel());
        container.addMessageListener(commentListener(), commentChannel());
        return container;
    }
}