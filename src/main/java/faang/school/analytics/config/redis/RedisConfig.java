package faang.school.analytics.config.redis;

import faang.school.analytics.messaging.GoalCompletedEventListener;
import faang.school.analytics.messaging.MentorshipRequestedEventListener;
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

    private final RedisConfigProperties redisProperties;

    @Bean
    MessageListenerAdapter MentorshipRequestedListener(MentorshipRequestedEventListener
                                                               mentorshipRequestedEventListener) {
        return new MessageListenerAdapter(mentorshipRequestedEventListener);
    }

    @Bean
    MessageListenerAdapter goalCompleteListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(),
                redisProperties.getPort());
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
    ChannelTopic mentorshipRequestTopic() {
        return new ChannelTopic(redisProperties.getChannel().getMentorshipRequest());
    }

    @Bean
    ChannelTopic goalCompleteTopic() {
        return new ChannelTopic(redisProperties.getChannel().getGoalCompleted());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter mentorshipRequestedListener,
                                                 MessageListenerAdapter goalListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipRequestedListener, mentorshipRequestTopic());
        container.addMessageListener(goalListener, goalCompleteTopic());
        return container;
    }
}
