package faang.school.analytics.config.redis;

import faang.school.analytics.listener.mentorshiprequest.MentorshipRequestedEventListener;
import faang.school.analytics.listener.recommendation.RecommendationEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channels.recommendation_topic}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channels.goal_topic}")
    private String goalCompletedChannel;

    @Value("${spring.data.redis.channels.mentorship-requested-topic}")
    private String mentorshipRequestedChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
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
    public Map<String, ChannelTopic> topics() {
        Map<String, ChannelTopic> result = new HashMap<>();
        result.put(RecommendationEventListener.class.getName(), new ChannelTopic(recommendationChannel));
        result.put(MentorshipRequestedEventListener.class.getName(), new ChannelTopic(mentorshipRequestedChannel));
//        result.put(GoalCompletedEventListener.class.getName(), new ChannelTopic(goalCompletedChannel));
        return result;
    }


    @Bean
    public RedisMessageListenerContainer redisContainer(List<MessageListener> listeners) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        Map<String, ChannelTopic> topics = topics();
        for (MessageListener listener : listeners) {
            container.addMessageListener(listener, topics.get(listener.getClass().getName()));
        }
        return container;
    }

}
