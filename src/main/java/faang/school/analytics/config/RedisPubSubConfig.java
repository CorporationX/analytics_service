package faang.school.analytics.config;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.subscriber.CommentEventListener;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {
    @Value("${spring.data.redis.topics.comment_event_topic}")
    private String topic;
    private final AnalyticsEventRepository analyticsEventRepository;
    @Bean
    ChannelTopic commentEventTopic() {
        return new ChannelTopic(topic);
    }
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new CommentEventListener(analyticsEventRepository, Mappers.getMapper(AnalyticsEventMapper.class)));
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), commentEventTopic());
        return container;
    }
}
