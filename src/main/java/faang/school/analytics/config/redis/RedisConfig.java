package faang.school.analytics.config.redis;

import faang.school.analytics.listener.AdBoughtEventListener;
import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.SubscriptionEventListener;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.listener.CommentEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final SubscriptionEventListener subscriptionEventListener;
    private final CommentEventListener commentEventListener;
    private final GoalCompletedEventListener goalCompletedEventListener;
    private final AdBoughtEventListener adBoughtEventListener;
    private final MentorshipRequestedEventListener mentorshipRequestedEventListener;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriptionEventListener, subscriptionTopic());
        container.addMessageListener(adBoughtEventListener, adBoughtTopic());
        container.addMessageListener(goalCompletedEventListener, goalCompletedTopic());
        container.addMessageListener(subscriptionEventListener, subscriptionTopic());
        container.addMessageListener(mentorshipRequestedEventListener, mentorshipRequestedTopic());
        container.addMessageListener(commentEventListener, commentChannel());
        container.addMessageListener(goalCompletedEventListener, goalCompletedChannel());
        log.info("Connection to Redis at port {} established.", redisProperties.port());
        return container;
    }

    @Bean
    public ChannelTopic adBoughtTopic() {
        return new ChannelTopic(redisProperties.channel().adBought());
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(redisProperties.channel().goalCompleted());
    }

    @Bean
    ChannelTopic subscriptionTopic() {
        return new ChannelTopic(redisProperties.channel().subscriptionChannel());
    }

    @Bean
    ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(redisProperties.channel().mentorshipRequest());
    }

    @Bean
    public ChannelTopic commentChannel() {
        return new ChannelTopic(redisProperties.channel().commentChannel());
    }

    @Bean
    public ChannelTopic goalCompletedChannel() {
        return new ChannelTopic(redisProperties.channel().goalCompletedChannel());
    }
}
