package faang.school.analytics.config.redis;

import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.listener.RecommendationEventListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

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
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter postViewEventListenerAdapter,
            SearchAppearanceEventListener searchAppearanceEventListener,
            MentorshipRequestedEventListener mentorshipRequestListenerAdapter,
            PremiumBoughtEventListener premiumBoughtEventListener,
            MessageListenerAdapter recommendationEventListenerAdapter,
            ChannelTopic buyPremiumTopic,
            ChannelTopic postViewTopic,
            ChannelTopic searchAppearanceTopic,
            ChannelTopic mentorshipChannel) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(postViewEventListenerAdapter, postViewTopic);
        container.addMessageListener(searchAppearanceEventListener, searchAppearanceTopic);
        container.addMessageListener(mentorshipRequestListenerAdapter, mentorshipChannel);
        container.addMessageListener(premiumBoughtEventListener, buyPremiumTopic);
        container.addMessageListener(recommendationEventListenerAdapter, recommendationEventTopic());
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
    public MessageListenerAdapter postViewEventListenerAdapter(PostViewEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }

    @Bean
    public ChannelTopic mentorshipChannel() {
        return new ChannelTopic(mentorshipChannel);
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestListenerAdapter(MentorshipRequestedEventListener listener) {
        return new MessageListenerAdapter(listener, mentorshipChannel);
    }

    @Bean
    public ChannelTopic buyPremiumTopic() {
        return new ChannelTopic(redisProperties.getBuyPremiumTopic());
    }

    @Bean
    public ChannelTopic recommendationEventTopic() {
        return new ChannelTopic(redisProperties.getRecommendationEventTopic());
    }

    @Bean
    public MessageListenerAdapter recommendationEventListenerAdapter(RecommendationEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}