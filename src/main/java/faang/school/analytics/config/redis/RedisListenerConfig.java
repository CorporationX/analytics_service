package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@Configuration
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        List<AbstractEventListener> listeners) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        listeners.forEach(listener -> {
            MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "onMessage");
            ChannelTopic topic = new ChannelTopic(listener.getChannelName());
            container.addMessageListener(adapter, topic);
        });

        return container;
    }

    @Bean
    public PremiumBoughtEventListener premiumBoughtEventListener(
            ObjectMapper objectMapper,
            MessageSource messageSource,
            AnalyticsEventService service,
            @Value("${spring.data.redis.channel.premium-bought}") String channelName,
            AnalyticsEventMapper mapper) {
        return new PremiumBoughtEventListener(objectMapper, messageSource, channelName, service, mapper);
    }

    @Bean
    public LikeEventListener likeEventListener(
            ObjectMapper objectMapper,
            MessageSource messageSource,
            AnalyticsEventService service,
            @Value("${spring.data.redis.channel.like_event_channel}") String channelName,
            AnalyticsEventMapper mapper) {
        return new LikeEventListener(objectMapper, messageSource, channelName, service, mapper);
    }
}