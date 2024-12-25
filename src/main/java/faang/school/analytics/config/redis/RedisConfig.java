package faang.school.analytics.config.redis;

import faang.school.analytics.listener.comment.CommentEventListener;
import faang.school.analytics.listener.event.FundRaisedEventListener;
import faang.school.analytics.listener.goal.GoalCompletedEventListener;
import faang.school.analytics.listener.premium.PremiumBoughtEventListener;
import faang.school.analytics.listener.project.ProjectViewEventListener;
import faang.school.analytics.listener.recommendation.RecommendationEventListener;
import faang.school.analytics.listener.subscription.FollowerEventListener;
import faang.school.analytics.listener.user.SearchAppearanceEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final GoalCompletedEventListener goalCompletedEventListener;
    private final ProjectViewEventListener projectViewEventListener;
    private final SearchAppearanceEventListener searchAppearanceEventListener;
    private final FundRaisedEventListener fundRaisedEventListener;
    private final FollowerEventListener followerEventListener;
    private final PremiumBoughtEventListener premiumBoughtEventListener;
    private final CommentEventListener commentEventListener;
    private final RecommendationEventListener recommendationEventListener;

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.channels.project-view-channel}")
    private String topicProjectView;
    @Value("${spring.data.redis.channels.fund-raised}")
    private String fundRaisedTopic;
    @Value("${spring.data.redis.channels.goal-completed}")
    private String topicGoalCompleted;
    @Value("${spring.data.redis.channels.search-appearance-channel}")
    private String topicSearchAppearance;
    @Value("${spring.data.redis.channels.follower}")
    private String topicFollower;
    @Value("${spring.data.redis.channels.premium-bought-channel}")
    private String premiumBoughtEventTopic;
    @Value("${spring.data.redis.channels.comment-channel}")
    private String topicComment;
    @Value("${spring.data.redis.channels.recommendation}")
    private String recommendationTopic;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
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
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        addMessageListenerInContainer(projectViewEventListener, topicProjectView, container);
        addMessageListenerInContainer(goalCompletedEventListener, topicGoalCompleted, container);
        addMessageListenerInContainer(searchAppearanceEventListener, topicSearchAppearance, container);
        addMessageListenerInContainer(fundRaisedEventListener, fundRaisedTopic, container);
        addMessageListenerInContainer(followerEventListener, topicFollower, container);
        addMessageListenerInContainer(premiumBoughtEventListener, premiumBoughtEventTopic, container);
        addMessageListenerInContainer(commentEventListener, topicComment, container);
        addMessageListenerInContainer(recommendationEventListener, recommendationTopic, container);

        return container;
    }

    private void addMessageListenerInContainer(MessageListener listenerAdapter, String topic, RedisMessageListenerContainer container) {
        container.addMessageListener(new MessageListenerAdapter(listenerAdapter), new ChannelTopic(topic));
    }
}