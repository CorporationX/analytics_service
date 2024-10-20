package faang.school.analytics.config.redis;

import faang.school.analytics.listener.project.ProjectViewEventListener;
import faang.school.analytics.listener.like.LikeEventListener;
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

<<<<<<< HEAD
    private final RedisProperties propertiesConfig;
=======
    private final RedisProperties redisProperties;

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter projectViewEvent) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(projectViewEvent, projectViewEventTopic());
        container.addMessageListener(likeEventAdapter, likeEventsTopic());
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
>>>>>>> unicorn-master-stream6

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic likeEventsTopic() {
        return new ChannelTopic(propertiesConfig.getChannel().getLikeEvents());
    }

    @Bean
    ChannelTopic projectViewEventTopic() {
        return new ChannelTopic(redisProperties.getChannel().getProjectViewChannel());
    }

    @Bean
    MessageListenerAdapter projectViewEvent(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

<<<<<<< HEAD

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeEventAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventAdapter, likeEventsTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }
}
