package faang.school.analytics.config;


import faang.school.analytics.listener.RecommendationEventListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import faang.school.analytics.listener.ProfileViewEventListener;
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
    private final SearchAppearanceEventListener searchAppearanceEventListener;
    private final ProfileViewEventListener profileViewEventListener;
    private final RecommendationEventListener recommendationEventListener;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channels.search_appearance_channel.name}")
    private String searchAppearanceTopic;

    @Value("${spring.data.redis.channels.recommendation.name}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channels.profile-view-channel.name}")
    private String profileViewChannel;


    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    MessageListenerAdapter userSearchMessageListenerAdapter() {
        return new MessageListenerAdapter(searchAppearanceEventListener);
    }

    @Bean
    public MessageListenerAdapter profileViewEventListenerAdapter() {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    public MessageListenerAdapter recommendationListener() {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(searchAppearanceTopic);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewChannel);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(userSearchMessageListenerAdapter(), searchAppearanceTopic());
        container.addMessageListener(recommendationListener(), recommendationTopic());
        container.addMessageListener(profileViewEventListenerAdapter(), profileViewTopic());
        return container;
    }
}
