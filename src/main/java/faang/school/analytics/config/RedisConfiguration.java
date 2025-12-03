package faang.school.analytics.config;

import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.listener.RedisChannelEventListeners;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfiguration {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Value("${spring.data.redis.channel.mentorship-request}")
    private String mentorshipRequestChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public MessageListenerAdapter likeEventListenerAdapter(LikeEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestedEventListenerAdapter(MentorshipRequestedEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    ChannelTopic mentorshipRequestedEventTopic() {
        return new ChannelTopic(mentorshipRequestChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            ApplicationContext context,
            JedisConnectionFactory jedisConnectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        Map<String, RedisChannelEventListeners> listeners = context.getBeansOfType(RedisChannelEventListeners.class);

        listeners.values().forEach(listener -> {
            ChannelTopic topic = new ChannelTopic(listener.getChannel());
            container.addMessageListener(listener, topic);
        });

        return container;
    }
}
