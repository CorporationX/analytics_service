package faang.school.analytics.config.redis;

import faang.school.analytics.listener.ProjectViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter projectViewEvent) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(projectViewEvent, projectViewEventTopic());
        return container;
    }

    @Bean
    ChannelTopic projectViewEventTopic() {
        return new ChannelTopic(redisProperties.getChannel().getProjectViewChannel());
    }

    @Bean
    MessageListenerAdapter projectViewEvent(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
}
