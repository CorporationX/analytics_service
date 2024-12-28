package faang.school.analytics.config.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.ProjectViewEventListener;
import faang.school.analytics.message.consumer.ProfileViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Value("${spring.data.redis.channel.project-view}")
    private String projectViewChannel;

    @Bean
    public MessageListenerAdapter likeListenerAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeEventTopic);
    }

    @Bean
    public MessageListenerAdapter projectViewAdapter(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    public ChannelTopic projectViewTopic() {
        return new ChannelTopic(projectViewChannel);
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
    public RedisMessageListenerContainer redisContainer(
            @Qualifier("likeListenerAdapter") MessageListenerAdapter likeListener,
            @Qualifier("projectViewAdapter") MessageListenerAdapter projectViewAdapter,
            JedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(likeListener, likeTopic());
        container.addMessageListener(projectViewAdapter, projectViewTopic());

        ChannelTopic profileViewTopic = new ChannelTopic(profileViewChannel);
        MessageListenerAdapter profileViewMessageListenerAdapter = new MessageListenerAdapter(profileViewEventListener);
        container.addMessageListener(profileViewMessageListenerAdapter, profileViewTopic);

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
}
