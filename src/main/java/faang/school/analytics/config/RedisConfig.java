package faang.school.analytics.config;

import faang.school.analytics.listeners.FollowerEventListener;
import faang.school.analytics.listeners.ProfileViewEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.profile_view_channel.name}")
    private String profileViewTopic;
    @Value("${spring.data.redis.channels.follower_channel.name}")
    private String eventFollowerTopic;


    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }
    @Bean
    MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }
    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewTopic);
    }
    @Bean
    ChannelTopic eventFollowerTopic() {
        return new ChannelTopic(eventFollowerTopic);
    }
    @Bean
    RedisMessageListenerContainer redisContainer(ProfileViewEventListener profileViewEventListener,
                                                 MessageListenerAdapter followerListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(profileViewEventListener, profileViewTopic());
        container.addMessageListener(followerListener, eventFollowerTopic());
        return container;
    }

}
