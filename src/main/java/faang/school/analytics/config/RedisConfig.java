package faang.school.analytics.config;

import faang.school.analytics.redis.listener.CommentEventListener;
import faang.school.analytics.redis.listener.LikeEventListener;
import faang.school.analytics.redis.listener.MentorshipRequestedEventListener;
import faang.school.analytics.redis.listener.SearchAppearanceListener;
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

    private final LikeEventListener likeEventListener;
    private final MentorshipRequestedEventListener mentorshipRequestedEventListener;
    private final CommentEventListener commentEventListener;
    private final SearchAppearanceListener searchAppearanceListener;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.mentorship_requested_channel}")
    private String mentorshipRequestedTopic;
    @Value("${spring.data.redis.channel.like_channel}")
    private String likeEventTopic;
    @Value("${spring.data.redis.channel.comment_channel}")
    private String commentEventTopic;
    @Value("${spring.data.redis.channel.search_appearance_channel}")
    private String searchAppearanceEventTopic;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        MessageListenerAdapter mentorshipRequestedMessageListener = new MessageListenerAdapter(mentorshipRequestedEventListener);
        MessageListenerAdapter likeEventMessageListener = new MessageListenerAdapter(likeEventListener);
        MessageListenerAdapter commentEventMessageListener = new MessageListenerAdapter(commentEventListener);
        MessageListenerAdapter searchAppearanceEventMessageListener = new MessageListenerAdapter(searchAppearanceListener);


        container.addMessageListener(mentorshipRequestedMessageListener, new ChannelTopic(mentorshipRequestedTopic));
        container.addMessageListener(likeEventMessageListener, new ChannelTopic(likeEventTopic));
        container.addMessageListener(commentEventMessageListener, new ChannelTopic(commentEventTopic));
        container.addMessageListener(searchAppearanceEventMessageListener, new ChannelTopic(searchAppearanceEventTopic));
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}