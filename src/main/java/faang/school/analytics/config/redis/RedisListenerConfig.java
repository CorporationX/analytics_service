package faang.school.analytics.config.redis;

import faang.school.analytics.listener.MentorshipRequestListener;
import faang.school.analytics.listener.PostViewEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisListenerConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.mentorship-request}")
    private String mentorshipRequestTopic;

    @Value("${spring.data.redis.channel.post-view}")
    private String postViewChannelName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    ChannelTopic mentorshipRequestChannel() {
        return new ChannelTopic(mentorshipRequestTopic);
    }

    @Bean
    ChannelTopic postViewChannel() {
        return new ChannelTopic(postViewChannelName);
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestListenerAdapter(MentorshipRequestListener mentorshipRequestListener) {
        return new MessageListenerAdapter(mentorshipRequestListener);
    }

    @Bean
    public MessageListenerAdapter postViewListenerAdapter(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer(MessageListenerAdapter mentorshipRequestListenerAdapter,
                                                                  ChannelTopic mentorshipRequestChannel,
                                                                  MessageListenerAdapter postViewListenerAdapter,
                                                                  ChannelTopic postViewChannel) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipRequestListenerAdapter, mentorshipRequestChannel);
        container.addMessageListener(postViewListenerAdapter, postViewChannel);
        return container;
    }
}
