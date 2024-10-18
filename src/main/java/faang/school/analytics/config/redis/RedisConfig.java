package faang.school.analytics.config.redis;

import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.GoalEventListener;
import faang.school.analytics.listener.ProfileViewEventListener;
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
    public MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            FollowerEventListener followerListener,
            @Qualifier("profileViewListener") MessageListenerAdapter profileViewListener,
            @Qualifier("profileViewChannel") ChannelTopic profileViewChannel,
            @Qualifier("followerEventTopic") ChannelTopic followerEventTopic,
            GoalEventListener goalListener,
            @Qualifier("goalEventTopic") ChannelTopic goalEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerEventTopic);
        container.addMessageListener(goalListener, goalEventTopic);
        container.addMessageListener(profileViewListener, profileViewChannel);
        return container;
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
    public ChannelTopic profileViewChannel(
            @Value("${spring.data.redis.channels.profile-view-channel.name}") String profileViewChannelName) {
        return new ChannelTopic(profileViewChannelName);
    }
}
