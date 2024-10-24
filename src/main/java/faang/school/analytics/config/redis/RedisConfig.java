package faang.school.analytics.config.redis;

import faang.school.analytics.listener.FollowerEventListener;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@Setter
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final PremiumBoughtEventRedisConfig premiumConfig;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            List<EventRedisConfig> eventRedisConfigs
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        eventRedisConfigs.forEach(
            config -> container.addMessageListener(config.getAdapter(), config.getTopic())
        );
        return container;
    }

        @Bean
    MessageListenerAdapter goalListener(GoalEventListener goalEventListener) {
        return new MessageListenerAdapter(goalEventListener);
    }

    @Bean
    MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    MessageListenerAdapter projectViewListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    MessageListenerAdapter postLikeListener(PostLikeEventListener postLikeEventListener) {
        return new MessageListenerAdapter(postLikeEventListener);
    }

        @Bean(value = "projectViewEventTopic")
    ChannelTopic projectViewEventTopic(@Value("${spring.data.redis.channels.project-view-channel.name}") String name) {
        return new ChannelTopic(name);
    }

    @Bean(value = "followerEventTopic")
    ChannelTopic followerEventTopic(@Value("${spring.data.redis.channels.follower-channel.name}") String name) {
        return new ChannelTopic(name);
    }

    @Bean(value = "goalEventTopic")
    ChannelTopic goalEventTopic(@Value("${spring.data.redis.channels.goal-channel.name}") String name) {
        return new ChannelTopic(name);
    }

    @Bean(value = "profileViewTopic")
    ChannelTopic profileViewTopic(
            @Value("${spring.data.redis.channels.profile-view-channel.name}") String profileViewChannelName) {
        return new ChannelTopic(profileViewChannelName);
    }

    @Bean(value = "postLikeTopic")
    ChannelTopic postLikeTopic(
            @Value("${spring.data.redis.channels.like-channel.name}") String postLikeChannelName) {
        return new ChannelTopic(postLikeChannelName);
    }

}
