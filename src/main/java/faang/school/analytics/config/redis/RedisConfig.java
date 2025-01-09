package faang.school.analytics.config.redis;

import faang.school.analytics.listener.fundraised.FundRaisedEventListener;
import faang.school.analytics.listener.mentorshiprequest.MentorshipRequestedEventListener;
import faang.school.analytics.listener.postview.PostViewEventListener;
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

    @Value("${spring.data.redis.channel.recommendation_topic}")
    private String recommendationChannel;
    @Value("${spring.data.redis.channel.post-view}")
    private String postViewChannel;
    @Value("${spring.data.redis.channel.goal_topic}")
    private String goalCompletedChannel;
    @Value("${spring.data.redis.channel.mentorship-requested-topic}")
    private String mentorshipRequestedChannel;
    @Value("${spring.data.redis.channel.follower-event-topic}")
    private String followersChannel;
    @Value("${spring.data.redis.channel.fund-raised}")
    private String fundRaisedChannel;
    @Value("${spring.data.redis.channel.premium-bought}")
    private String premiumBoughtChannel;

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
        result.put(FollowerEventListener.class.getName(), new ChannelTopic(followersChannel));
        result.put(FundRaisedEventListener.class.getName(), new ChannelTopic(fundRaisedChannel));
        result.put(PremiumBoughtEventListener.class.getName(), new ChannelTopic(premiumBoughtChannel));
        result.put(PostViewEventListener.class.getName(), new ChannelTopic(postViewChannel));
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
