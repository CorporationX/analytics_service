package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.like.LikeEventDto;
import faang.school.analytics.listener.EventListener;
import faang.school.analytics.listener.like.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.like-event}")
    private String likeEvents;

    private final LikeEventListener likeEventListener;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host,port);
        return new JedisConnectionFactory(redisConfig);
    }

//    @Bean
//    public RedisMessageListenerContainer redisContainer(JedisConnectionFactory jedisConnectionFactory,
//                                                        MessageListenerAdapter likeEventsListener) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(jedisConnectionFactory());
//        container.addMessageListener(likeEventListener, likeEventsTopic());
//        return container;
//    }
//    @Bean
//    public RedisTemplate<String, LikeEventDto> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
//        RedisTemplate<String, LikeEventDto> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory);
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
//        template.setValueSerializer(serializer);
//        return template;
//    }

    @Bean
    public RedisTemplate<String, LikeEventDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, LikeEventDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        LikeEventListener listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new MessageListenerAdapter(listener), new ChannelTopic("your-topic"));
        return container;
    }
    @Bean
    public MessageListenerAdapter likeEventListener() {
        return new MessageListenerAdapter(likeEventListener);
    }
    @Bean
    public ChannelTopic likeEventsTopic() {
        return new ChannelTopic(likeEvents);
    }
}
