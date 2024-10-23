package faang.school.analytics.config.redis;

import faang.school.analytics.listener.AdBoughtEventListener;
import faang.school.analytics.listener.CommentEventListener;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.FundRaisedEventListener;
import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.listener.ProjectViewEventListener;
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

    @Value("${spring.data.redis.channels.post-view-channel.name}")
    private String postViewEvent;

    @Value("${spring.data.redis.channels.mentorship-request-channel.name}")
    private String mentorshipRequestTopic;

    @Value("${spring.data.redis.channels.fund-raised-channel.name}")
    private String fundRaisedChannel;

    @Value("${spring.data.redis.channels.ad-bought-channel.name}")
    private String adBoughtEvent;

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
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter followerListener,
                                                 MessageListenerAdapter likeListener,
                                                 MessageListenerAdapter goalCompletedListener,
                                                 MessageListenerAdapter commentListener,
                                                 MessageListenerAdapter projectViewListener,
                                                 MessageListenerAdapter premiumBoughtListener,
                                                 MessageListenerAdapter postViewListener,
                                                 MessageListenerAdapter mentorshipRequestListener,
                                                 MessageListenerAdapter fundRaisedListener,
                                                 MessageListenerAdapter adBoughtListener) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(goalCompletedListener, goalCompletedTopic());
        container.addMessageListener(likeListener, likeTopic());
        container.addMessageListener(commentListener, commentTopic());
        container.addMessageListener(projectViewListener, projectViewTopic());
        container.addMessageListener(premiumBoughtListener, premiumBoughtTopic());
        container.addMessageListener(postViewListener, postViewEventTopic());
        container.addMessageListener(mentorshipRequestListener, mentorshipRequestTopic());
        container.addMessageListener(fundRaisedListener, fundRaisedTopic());
        container.addMessageListener(adBoughtListener, adBoughtTopic());
        return container;
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
    MessageListenerAdapter postViewListener(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    MessageListenerAdapter mentorshipRequestListener(MentorshipRequestedEventListener mentorshipRequestedEventListener) {
        return new MessageListenerAdapter(mentorshipRequestedEventListener);
    }

    @Bean
    MessageListenerAdapter fundRaisedListener(FundRaisedEventListener fundRaisedEventListener) {
        return new MessageListenerAdapter(fundRaisedEventListener);
    }

    @Bean
    MessageListenerAdapter adBoughtListener(AdBoughtEventListener adBoughtEventListener) {
        return new MessageListenerAdapter(adBoughtEventListener);
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
    ChannelTopic postViewEventTopic() {
        return new ChannelTopic(postViewEvent);
    }

    @Bean
    public ChannelTopic mentorshipRequestTopic() {
        return new ChannelTopic(mentorshipRequestTopic);
    }

    @Bean
    public ChannelTopic fundRaisedTopic() {
        return new ChannelTopic(fundRaisedChannel);
    }

    @Bean
    ChannelTopic adBoughtTopic() {
        return new ChannelTopic(adBoughtEvent);
    }
}
