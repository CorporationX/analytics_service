package faang.school.analytics.config.redis;

import faang.school.analytics.listener.like.LikeEventListener;
import faang.school.analytics.listener.project.ProjectViewEventListener;
import faang.school.analytics.listener.post.PostViewEventListener;
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
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter projectViewEvent,
                                                 MessageListenerAdapter likeEventAdapter,
                                                 MessageListenerAdapter postViewEvent) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(projectViewEvent, projectViewEventTopic());
        container.addMessageListener(likeEventAdapter, likeEventsTopic());
        container.addMessageListener(postViewEvent, postViewEventTopic());
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
    public ChannelTopic likeEventsTopic() {
        return new ChannelTopic(redisProperties.getChannel().getLikeEvents());
    }

    @Bean
    public MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }
    @Bean
    public ChannelTopic postViewEventTopic() {
        return new ChannelTopic(redisProperties.getChannel().getPostViewEvent());
    }

    @Bean
    MessageListenerAdapter postViewEvent(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }
}
