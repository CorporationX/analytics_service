package faang.school.analytics.config.redis;

import faang.school.analytics.listener.*;
import faang.school.analytics.model.event.SearchAppearance.InputSearchAppearanceEvent;
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

    @Value("${spring.data.redis.channels.like-channel.name}")
    private String likeChannelName;
    @Value("${spring.data.redis.channels.follower-event-channel.name}")
    private String followerEvent;
    @Value("${spring.data.redis.channels.goal-completed-event-channel.name}")
    private String goalCompletedEvent;
    @Value("${spring.data.redis.channels.comment-event-channel.name}")
    private String commentEvent;
    @Value("${spring.data.redis.channels.project-view-channel.name}")
    private String projectViewTopic;
    @Value("${spring.data.redis.channels.premium-bought-channel.name}")
    private String premiumBoughtTopic;
    @Value("${spring.data.redis.channels.search-appearance-channel.name}")
    private String searchAppearanceTopic;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter premiumBoughtListener(PremiumBoughtEventListener premiumBoughtEventListener) {
        return new MessageListenerAdapter(premiumBoughtEventListener);
    }

    @Bean
    MessageListenerAdapter goalCompletedListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    MessageListenerAdapter projectViewListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    MessageListenerAdapter searchAppearanceEventListener(InputSearchAppearanceEventListener inputSearchAppearanceEventListener) {
        return new MessageListenerAdapter(inputSearchAppearanceEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter followerListener,
                                                 MessageListenerAdapter likeListener,
                                                 MessageListenerAdapter goalCompletedListener,
                                                 MessageListenerAdapter commentListener,
                                                 MessageListenerAdapter projectViewListener,
                                                 MessageListenerAdapter premiumBoughtListener,
                                                 MessageListenerAdapter searchAppearanceEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(goalCompletedListener, goalCompletedTopic());
        container.addMessageListener(likeListener, likeTopic());
        container.addMessageListener(commentListener, commentTopic());
        container.addMessageListener(projectViewListener, projectViewTopic());
        container.addMessageListener(premiumBoughtListener, premiumBoughtTopic());
        container.addMessageListener(searchAppearanceEventListener, searchAppearanceTopic());

        return container;
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannelName);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerEvent);
    }

    @Bean
    ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedEvent);
    }

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(commentEvent);
    }

    @Bean
    ChannelTopic projectViewTopic() {
        return new ChannelTopic(projectViewTopic);
    }

    @Bean
    ChannelTopic premiumBoughtTopic() {
        return new ChannelTopic(premiumBoughtTopic);
    }

    @Bean
    ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(searchAppearanceTopic);
    }
}
