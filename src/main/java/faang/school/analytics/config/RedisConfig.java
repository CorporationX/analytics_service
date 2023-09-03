package faang.school.analytics.config;

import faang.school.analytics.redis.listener.LikeEventListener;
import faang.school.analytics.redis.listener.MentorshipRequestedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.mentorship_requested_channel}")
    private String mentorshipRequestedTopic;
    @Value("${spring.data.redis.channel.like_channel}")
    private String likeEventTopic;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter mentorshipRequestedMessageListener,
                                                                       MessageListenerAdapter likeEventMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipRequestedMessageListener, mentorshipRequestedTopic());
        container.addMessageListener(likeEventMessageListener, likeEventTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestedMessageListener(MentorshipRequestedEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter likeEventMessageListener(LikeEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(mentorshipRequestedTopic);
    }
    @Bean
    public ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeEventTopic);
    }
}