package faang.school.analytics.config;

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

    @Value("${spring.data.redis.channel.mentorship-requested-topic}")
    private String mentorshipRequestedChannel;

    @Value("${spring.data.redis.channels.recommendation_topic}")
    private String recommendationChannel;

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
    public MessageListenerAdapter mentorshipRequestedListener(MentorshipRequestedEventListener listener) {
        return new MessageListenerAdapter(listener);
    public MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(mentorshipRequestedChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter mentorshipRequestedListener) {
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipRequestedListener, mentorshipRequestedTopic());
        container.addMessageListener(recommendationListener, recommendationTopic());
        return container;
    }

}
