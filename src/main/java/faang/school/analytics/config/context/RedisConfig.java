package faang.school.analytics.config.context;

import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer listenerContainer(
            RedisConnectionFactory factory, MessageListenerAdapter adapter
    ) {
        log.info("Creating redis listener container for analytics");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(adapter, new ChannelTopic("analytics"));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(AnalyticsEventService analyticsEventService) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(analyticsEventService,
                "saveAnalytics");
        adapter.setSerializer(new StringRedisSerializer());
        return adapter;
    }
}
