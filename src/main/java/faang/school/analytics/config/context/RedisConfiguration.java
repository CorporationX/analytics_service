package faang.school.analytics.config.context;

import faang.school.analytics.listener.RedisLikeEventListener;
import faang.school.analytics.listener.RedisMessageListener;
import faang.school.analytics.publisher.MessagePublisher;
import faang.school.analytics.publisher.RedisMessagePublisher;
import faang.school.analytics.subscriber.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("{spring.data.redis.channel.like-event")
    private String likeEventTopic;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    } // тимплейт

    @Bean
    MessageListenerAdapter likeEventListener(RedisLikeEventListener redisLikeEventListener) {
        return new MessageListenerAdapter(redisLikeEventListener);
    } // адаптер RedisLikeEventListener

    @Bean
    MessageListenerAdapter messageListener(RedisMessageListener redisMessageListener) {
        return new MessageListenerAdapter(redisMessageListener);
    } // адаптер RedisMessageListener

    @Bean
    public RedisMessageListenerContainer likeEventContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListener likeEventConsumer = likeEventConsumer();
        container.addMessageListener(likeEventConsumer, likeEventTopic());

        return container;
    } // контейнер для LikeEvent

    @Bean
    public MessageListener likeEventConsumer() {
        return new RedisLikeEventListener();
    } // листенер/консьюмер для LikeEvent


    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListener, MessageListenerAdapter likeEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisMessageListener);
        container.addMessageListener(messageListener, topic());
        container.addMessageListener(likeEventListener, likeEventTopic());

        return container;
    } // контейнер для MessageListener

    @Bean
    MessagePublisher redisPublisher(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = redisTemplate(connectionFactory);
        return new RedisMessagePublisher(template, topic());
    } // паблишер

    @Bean
    ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeEventTopic);
    } // надо настроить тему

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("messageQueue");
    } // надо настроить тему
}
