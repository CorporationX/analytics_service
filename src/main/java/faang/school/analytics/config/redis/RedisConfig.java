package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.MentorShipRequestListener;
import faang.school.analytics.listener.PostViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper mapper;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.goal-completing-channel}")
    private String goalEventsTopic;

    @Value("${spring.data.redis.channel.PostViewEvent}")
    private String channelPostViewEvent;

    @Value("${spring.data.redis.channel.mentorship-request-channel}")
    private String channelMentorshipRequest;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);
        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            List<MessageListenerAdapter> listeners,
            List<ChannelTopic> topics) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        for (int i = 0; i < listeners.size(); i++) {
            container.addMessageListener(listeners.get(i), topics.get(i));
        }

        return container;
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalEventsTopic);
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(GoalCompletedEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public ChannelTopic PostViewTopic() {
        return new ChannelTopic(channelPostViewEvent);
    }

    @Bean
    public MessageListenerAdapter postViewListenerAdapter(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public ChannelTopic MentorShipRequestTopic() {
        return new ChannelTopic(channelMentorshipRequest);
    }

    @Bean
    public MessageListenerAdapter mentorShipListener(MentorShipRequestListener mentorShipRequestListener) {
        return new MessageListenerAdapter(mentorShipRequestListener);
    }
}
