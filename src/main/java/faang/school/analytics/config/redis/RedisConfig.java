package faang.school.analytics.config.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.message.consumer.LikeEventListener;
import faang.school.analytics.message.consumer.ProfileViewEventListener;
import faang.school.analytics.message.consumer.ViewedUsersEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ProfileViewEventListener profileViewEventListener;

    @Value("${spring.data.redis.channel.like-event-topic}")
    private String likeEventTopic;

    @Value("${spring.data.redis.channel.profile-view}")
    private String profileViewChannel;

    @Value("${spring.data.redis.channel.viewed-user-event-channel}")
    private String viewedUserChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisSerializer<Object> jackson2JsonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .findAndRegisterModules();

        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeListener,
                                                        MessageListenerAdapter viewedUserEventListener,
                                                        JedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(likeListener, likeTopic());

        ChannelTopic profileViewTopic = new ChannelTopic(profileViewChannel);
        MessageListenerAdapter profileViewMessageListenerAdapter = new MessageListenerAdapter(profileViewEventListener);
        container.addMessageListener(profileViewMessageListenerAdapter, profileViewTopic);
        container.addMessageListener(viewedUserEventListener, viewedUsersTopic());

        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeEventTopic);
    }

    @Bean
    public ChannelTopic viewedUsersTopic() {
        return new ChannelTopic(viewedUserChannel);
    }

    @Bean
    public MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public MessageListenerAdapter viewedUserEventListener(ViewedUsersEventListener viewedUsersEventListener) {
        return new MessageListenerAdapter(viewedUsersEventListener);
    }
}
