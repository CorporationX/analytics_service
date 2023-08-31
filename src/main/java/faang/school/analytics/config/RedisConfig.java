package faang.school.analytics.config;

import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.listener.RecommendationEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.recommendation}")
    private String recommendationChannel;
    @Value("${spring.data.redis.channels.post_view}")
    private String postViewChannel;
    @Value("${spring.data.redis.channels.mentorship_requested_channel.name}")
    private String mentorshipRequestedTopic;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Crated redis connection factory with host: {}, port: {}", host, port);
        return new JedisConnectionFactory();
    }

    @Bean
    public MessageListenerAdapter recommendationAdapter(RecommendationEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter postViewAdapter(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestedMessageListener(MentorshipRequestedEventListener listener) {
        return new MessageListenerAdapter(listener);

    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewChannel);
    }

    @Bean
    public ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(mentorshipRequestedTopic);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationAdapter,
                                                        MessageListenerAdapter postViewAdapter,
                                                        MessageListenerAdapter mentorshipRequestedMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationAdapter, recommendationTopic());
        container.addMessageListener(postViewAdapter, postViewTopic());
        container.addMessageListener(mentorshipRequestedMessageListener, mentorshipRequestedTopic());
        return container;
    }
}
