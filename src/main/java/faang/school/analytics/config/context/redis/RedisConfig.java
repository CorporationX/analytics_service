package faang.school.analytics.config.context.redis;

import faang.school.analytics.listener.RedisMessagePublisher;
import faang.school.analytics.listener.RedisMessageSub;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
@AllArgsConstructor
@Configuration
public class RedisConfig {
    private final RedisMessageSub redisMessageConsumer;
    public final RedisMessagePublisher redisMessagePublisher;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(redisMessageConsumer, new ChannelTopic("view-project"));

        return container;
    }


    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("view-project");
    }

}
