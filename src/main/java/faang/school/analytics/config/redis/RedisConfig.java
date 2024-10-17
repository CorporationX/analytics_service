package faang.school.analytics.config.redis;

import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
public class RedisConfig {

    private final PremiumBoughtEventRedisConfig premiumConfig;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            FollowerEventListener followerListener,
            @Qualifier("followerEventTopic") ChannelTopic followerEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerEventTopic);
        container.addMessageListener(premiumConfig.premiumAdapter(), premiumConfig.premiumChannel());
        return container;
    }

    @Bean(value = "followerEventTopic")
    ChannelTopic followerEventTopic(@Value("${spring.data.redis.channels.follower-channel.name}") String name) {
        return new ChannelTopic(name);
    }
}
