package faang.school.analytics.config.redis;

import faang.school.analytics.messaging.LikeEventListener;
import faang.school.analytics.service.redis.listeners.PremiumEventsListener;
import faang.school.analytics.service.redis.listeners.ProjectEventsListener;
import faang.school.analytics.service.redis.listeners.UserEventsListener;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {

    @Value("${spring.data.redis.channels.user_events_channel.name}")
    private String userEventChannelName;
    private final UserEventsListener userEventsListener;

    @Value("${spring.data.redis.channels.project_events_channel.name}")
    private String projectEventChannelName;
    private final ProjectEventsListener projectEventsListener;

    @Value("${spring.data.redis.channels.premium_events_channel.name}")
    private String premiumEventChannelName;
    private final PremiumEventsListener premiumEventsListener;

    @Value("${spring.data.redis.channels.like_events_channel.name}")
    private String likeEventChannelName;
    private final LikeEventListener likeEventListener;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        MessageListenerAdapter userEventListenerAdapter = new MessageListenerAdapter(userEventsListener);
        MessageListenerAdapter projectEventListenerAdapter = new MessageListenerAdapter(projectEventsListener);
        MessageListenerAdapter premiumEventListenerAdapter = new MessageListenerAdapter(premiumEventsListener);
        MessageListenerAdapter likeEventsListenerAdapter = new MessageListenerAdapter(likeEventListener);

        container.addMessageListener(userEventListenerAdapter, new ChannelTopic(userEventChannelName));
        container.addMessageListener(projectEventListenerAdapter, new ChannelTopic(projectEventChannelName));
        container.addMessageListener(premiumEventListenerAdapter, new ChannelTopic(premiumEventChannelName));
        container.addMessageListener(likeEventsListenerAdapter, new ChannelTopic(likeEventChannelName));

        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
