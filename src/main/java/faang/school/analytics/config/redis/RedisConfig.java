package faang.school.analytics.config.redis;

import faang.school.analytics.listener.AdBoughtEventListener;
import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.SubscriptionEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final SubscriptionEventListener subscriptionEventListener;
    private final GoalCompletedEventListener goalCompletedEventListener;
    private final AdBoughtEventListener adBoughtEventListener;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriptionEventListener, subscriptionTopic());
        container.addMessageListener(adBoughtEventListener, adBoughtTopic());
        container.addMessageListener(goalCompletedEventListener, goalCompletedTopic());
        container.addMessageListener(subscriptionEventListener, subscriptionTopic());
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
}
