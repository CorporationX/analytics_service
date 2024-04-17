package faang.school.analytics.config.redis;

import faang.school.analytics.listeners.SearchAppearanceEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channels.profile_search_channel.name}")
    private String profileSearchChannelName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        System.out.println(redisPort);
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter profileSearchListener(SearchAppearanceEventListener searchAppearanceEventListener) {
        return new MessageListenerAdapter(profileSearchChannelName);
    }

    @Bean
    public ChannelTopic profileSearchTopic() {
        return new ChannelTopic(profileSearchChannelName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter profileSearchListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        // Добавляем слушателя на первый канал
        container.addMessageListener(profileSearchListener, profileSearchTopic());
        // Добавляем слушателя сообщений на последующие каналы + не забудь в параметрах указывать MessageListenerAdapter (это что бы не забыть =)
        return container;
    }
}
