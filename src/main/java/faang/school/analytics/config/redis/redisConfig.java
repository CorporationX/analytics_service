package faang.school.analytics.config.redis;

import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.ProjectViewEventListener;
import faang.school.analytics.listener.UserSearchAppearanceEventListener;
import faang.school.analytics.messageListener.RecommendationEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class redisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.goal_completed_topic.name}")
    private String goalCompletedTopic;

    @Value("${spring.data.redis.channel.recommendation_topic}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channel.project_view_channel.name}")
    private String projectViewTopic;

    @Value("${spring.data.redis.channel.user_search_appearance_topic.name}")
    private String userSearchAppearanceTopic;

    private final GoalCompletedEventListener goalCompletedEventListener;
    private final RecommendationEventListener recommendationEventListener;
    private final ProjectViewEventListener projectViewEventListener;
    private final UserSearchAppearanceEventListener userSearchAppearanceEventListener;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedTopic);
    }

    @Bean
    public MessageListenerAdapter GoalCompletedMessageListener() {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public MessageListenerAdapter recommendationListener() {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    ChannelTopic projectViewTopic() {
        return new ChannelTopic(projectViewTopic);
    }

    @Bean
    public MessageListenerAdapter projectViewListener() {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    ChannelTopic userSearchAppearanceTopic() {
        return new ChannelTopic(userSearchAppearanceTopic);
    }

    @Bean
    public MessageListenerAdapter userSearchAppearanceEventListenerAdapter() {
        return new MessageListenerAdapter(userSearchAppearanceEventListener);
    }

    @Bean
    public RedisMessageListenerContainer container(JedisConnectionFactory jedisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(GoalCompletedMessageListener(), goalCompletedTopic());
        container.addMessageListener(recommendationListener(), recommendationTopic());
        container.addMessageListener(projectViewListener(), projectViewTopic());
        container.addMessageListener(userSearchAppearanceEventListenerAdapter(), userSearchAppearanceTopic());
        return container;
    }
}
