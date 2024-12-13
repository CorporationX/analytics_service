package faang.school.analytics.config.redis;

import faang.school.analytics.listener.NewCommentEventListener;
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
    private final NewCommentEventListener newCommentEventListener;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(newCommentEventListener, commentChannel());
        log.info("Connection to Redis at port {} established.", redisProperties.port());
        return container;
    }

    @Bean
    public ChannelTopic commentChannel() {
        return new ChannelTopic(redisProperties.channel().commentChannel());
    }
}
