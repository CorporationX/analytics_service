package faang.school.analytics.config;

import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.ProfileViewEventListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
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
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.profile_view_channel.name}")
    private String profileViewTopic;
    @Value("${spring.data.redis.channels.follower_channel.name}")
    private String eventFollowerTopic;
    @Value("${spring.data.redis.channels.profile_search_channel.name}")
    private String userProfileSearchTopic;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfig);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
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
    public MessageListenerAdapter searchAppearanceListener(SearchAppearanceEventListener searchAppearanceEventListener) {
        return new MessageListenerAdapter(searchAppearanceEventListener);
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
    public ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(userProfileSearchTopic);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter profileViewListener,
                                                 MessageListenerAdapter followerListener,
                                                 MessageListenerAdapter searchAppearanceListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(profileViewListener, profileViewTopic());
        container.addMessageListener(followerListener, eventFollowerTopic());
        container.addMessageListener(searchAppearanceListener, searchAppearanceTopic());
        return container;
    }
}