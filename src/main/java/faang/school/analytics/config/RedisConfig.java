package faang.school.analytics.config;

import faang.school.analytics.listener.*;
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
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.follower-event}")
    private String followerEventChannel;

    @Value("${spring.data.redis.channel.search-appearance}")
    private String searchAppearanceChannel;

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channel.ad-bought}")
    private String adBoughtChannel;

    @Value("${spring.data.redis.channel.profile-view}")
    private String profileViewChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    MessageListenerAdapter likeEvent(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    MessageListenerAdapter recommendationEvent(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    MessageListenerAdapter followerEvent (FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter searchAppearanceEvent(SearchAppearanceEventListener searchAppearanceEventListener) {
        return new MessageListenerAdapter(searchAppearanceEventListener);
    }

    @Bean
    MessageListenerAdapter adBoughtEvent(AdBoughtEventListener adBoughtEventListener) {
        return new MessageListenerAdapter(adBoughtEventListener);
    }

    @Bean
    MessageListenerAdapter profileViewEvent(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(searchAppearanceChannel);
    }

    @Bean
    ChannelTopic adBoughtTopic() {
        return new ChannelTopic(adBoughtChannel);
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewChannel);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerEventChannel);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter searchAppearanceEvent,
                                                 MessageListenerAdapter likeEvent,
                                                 MessageListenerAdapter recommendationEvent,
                                                 MessageListenerAdapter adBoughtEvent,
                                                 MessageListenerAdapter profileViewEvent,
                                                 MessageListenerAdapter followerEvent) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        container.addMessageListener(likeEvent, likeTopic());
        container.addMessageListener(recommendationEvent, recommendationTopic());
        container.addMessageListener(searchAppearanceEvent, searchAppearanceTopic());
        container.addMessageListener(adBoughtEvent, adBoughtTopic());
        container.addMessageListener(profileViewEvent, profileViewTopic());
        container.addMessageListener(followerEvent, followerTopic());
        return container;
    }
}