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

}
