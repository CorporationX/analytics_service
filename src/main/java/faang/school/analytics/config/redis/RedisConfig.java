package faang.school.analytics.config.redis;

import faang.school.analytics.listener.ad.AdBoughtEventListener;
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
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.mentorship-requested-topic}")
    private String mentorshipRequestedChannel;

    @Value("${spring.data.redis.channel.recommendation_topic}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channel.fund-raised}")
    private String fundRaisedChannel;

    @Value("${spring.data.redis.channel.ad_bought}")
    private String adBoughtChannel;

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
    public MessageListenerAdapter mentorshipRequestedListener(MentorshipRequestedEventListener mentorshipRequestedEventListener) {
        return new MessageListenerAdapter(mentorshipRequestedEventListener);
    }

    @Bean
    public MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public MessageListenerAdapter adBoughtListener(AdBoughtEventListener adBoughtEventListener) {
        return new MessageListenerAdapter(adBoughtEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(mentorshipRequestedChannel);
    }

    @Bean
    public ChannelTopic fundRaisedTopic() {
        return new ChannelTopic(fundRaisedChannel);
    }

    @Bean
    public ChannelTopic adBoughtTopic() {
        return new ChannelTopic(adBoughtChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter mentorshipRequestedListener,
                                                        MessageListenerAdapter recommendationListener,
                                                        MessageListener fundRaisedEventListener,
                                                        MessageListenerAdapter adBoughtListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipRequestedListener, mentorshipRequestedTopic());
        container.addMessageListener(recommendationListener, recommendationTopic());
        container.addMessageListener(fundRaisedEventListener, fundRaisedTopic());
        container.addMessageListener(adBoughtListener, adBoughtTopic());
        return container;
    }

}
