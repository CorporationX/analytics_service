package faang.school.analytics.config.redis;


import faang.school.analytics.messaging.LikeEventListener;
import faang.school.analytics.service.redis.listeners.PremiumEventsListener;
import faang.school.analytics.service.redis.listeners.ProjectEventsListener;
import faang.school.analytics.service.redis.listeners.UserEventsListener;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.context.annotation.Bean;

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

        container.addMessageListener(userEventsListener, new ChannelTopic(userEventChannelName));
        container.addMessageListener(projectEventsListener, new ChannelTopic(projectEventChannelName));
        container.addMessageListener(premiumEventsListener, new ChannelTopic(premiumEventChannelName));
        container.addMessageListener(likeEventListener, new ChannelTopic(likeEventChannelName));

        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
