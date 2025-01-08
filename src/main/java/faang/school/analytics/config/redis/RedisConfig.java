package faang.school.analytics.config.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.listener.RecommendationEventListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.topic.search-appearance}")
    private String searchAppearanceTopicName;
    @Value("${spring.data.redis.topic.view-appearance}")
    private String postViewTopicName;
    @Value("${spring.data.redis.topic.mentorship-channel}")
    private String mentorshipChannel;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }
    @Bean
    @Qualifier("redisObjectMapper")
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory,
            @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter postViewEventListenerAdapter,
            MessageListenerAdapter searchAppearanceEventListenerAdapter,
            MessageListenerAdapter mentorshipRequestListenerAdapter,
            MessageListenerAdapter premiumBoughtEventListenerAdapter,
            MessageListenerAdapter recommendationEventListenerAdapter,
            @Qualifier("recommendationTopic") ChannelTopic recommendationEventTopic,
            ChannelTopic buyPremiumTopic,
            ChannelTopic postViewTopic,
            ChannelTopic searchAppearanceTopic,
            ChannelTopic mentorshipChannel) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(postViewEventListenerAdapter, postViewTopic);
        container.addMessageListener(searchAppearanceEventListenerAdapter, searchAppearanceTopic);
        container.addMessageListener(mentorshipRequestListenerAdapter, mentorshipChannel);
        container.addMessageListener(premiumBoughtEventListenerAdapter, buyPremiumTopic);
        container.addMessageListener(recommendationEventListenerAdapter, recommendationEventTopic);

        return container;
    }

    @Bean
    public ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(searchAppearanceTopicName);
    }

    @Bean
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewTopicName);
    }

    @Bean
    public ChannelTopic mentorshipChannel() {
        return new ChannelTopic(mentorshipChannel);
    }

    @Bean
    public ChannelTopic buyPremiumTopic() {
        return new ChannelTopic(redisProperties.getBuyPremiumTopic());
    }

    @Bean
    @Qualifier("recommendationTopic")
    public ChannelTopic recommendationEventTopic() {
        return new ChannelTopic(redisProperties.getRecommendationEventTopic());
    }

    @Bean
    public MessageListenerAdapter postViewEventListenerAdapter(PostViewEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter searchAppearanceEventListenerAdapter(SearchAppearanceEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestListenerAdapter(MentorshipRequestedEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter premiumBoughtEventListenerAdapter(PremiumBoughtEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter recommendationEventListenerAdapter(RecommendationEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}