package faang.school.analytics.config.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.listener.ProjectViewEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.project-view}")
    private String projectView;

    @Bean
    JedisConnectionFactory JedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }

    @Bean
    ChannelTopic projectViewTopic() {
        return new ChannelTopic(projectView);
    }

    @Bean
    MessageListenerAdapter projectViewListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter projectViewEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(JedisConnectionFactory());
        container.addMessageListener(projectViewEventListener, projectViewTopic());
        return container;
    }
}
