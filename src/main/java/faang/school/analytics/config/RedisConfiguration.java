package faang.school.analytics.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
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
public class RedisConfiguration {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new LikeEventListener());
    }

    @Bean
    public MessageListenerAdapter listenerSearchAppearanceEvent(SearchAppearanceEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    public ChannelTopic topicSearchAppearance(@Value("${spring.data.redis.channel.search-appearance}") String topic) {
        return new ChannelTopic(topic);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory,
                                                       ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListener,
                                                        MessageListenerAdapter listenerSearchAppearanceEvent,
                                                        ChannelTopic likeTopic,
                                                        ChannelTopic topicSearchAppearance,
                                                        JedisConnectionFactory jedisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(messageListener, likeTopic());
        container.addMessageListener(listenerSearchAppearanceEvent, topicSearchAppearance);
        return container;
    }
}
