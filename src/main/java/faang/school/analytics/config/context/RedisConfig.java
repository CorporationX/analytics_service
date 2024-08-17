package faang.school.analytics.config.context;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            List<Pair<MessageListenerAdapter, ChannelTopic>> redisEventListener,
            JedisConnectionFactory jedisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        for (Pair<MessageListenerAdapter, ChannelTopic> messageListenerAdapter : redisEventListener) {
            container.addMessageListener(messageListenerAdapter.getFirst(), messageListenerAdapter.getSecond());
        }
        return container;
    }
}
