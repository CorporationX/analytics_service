package faang.school.analytics.config.redis;

import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.GoalEventListener;
import faang.school.analytics.listener.ProfileViewEventListener;
import faang.school.analytics.listener.ProjectViewEventListener;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Setter
@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter goalListener(GoalEventListener goalEventListener) {
        return new MessageListenerAdapter(goalEventListener);
    }

    @Bean(value = "profileViewListener")
    MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    MessageListenerAdapter projectViewListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            FollowerEventListener followerListener,
            @Qualifier("followerEventTopic") ChannelTopic followerEventTopic,
            GoalEventListener goalListener,
            @Qualifier("goalEventTopic") ChannelTopic goalEventTopic,
            ProjectViewEventListener projectViewListener,
            @Qualifier("projectViewEventTopic") ChannelTopic projectViewEventTopic,
            @Qualifier("profileViewListener") MessageListenerAdapter profileViewListener,
            @Qualifier("profileViewChannel") ChannelTopic profileViewChannel
            ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerEventTopic);
        container.addMessageListener(goalListener, goalEventTopic);
        container.addMessageListener(profileViewListener, profileViewChannel);
        container.addMessageListener(projectViewListener, projectViewEventTopic);
        return container;
    }

    @Bean(value = "projectViewEventTopic")
    ChannelTopic projectViewEventTopic(@Value("${spring.data.redis.channels.project-view-channel.name}")String name) {
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

    @Bean(value = "profileViewChannel")
    ChannelTopic profileViewChannel(
            @Value("${spring.data.redis.channels.profile-view-channel.name}") String profileViewChannelName) {
        return new ChannelTopic(profileViewChannelName);
    }
}
