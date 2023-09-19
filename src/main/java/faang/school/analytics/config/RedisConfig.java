package faang.school.analytics.config;

import faang.school.analytics.listener.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.recommendation}")
    private String recommendationChannel;
    @Value("${spring.data.redis.channels.post_view}")
    private String postViewChannel;
    @Value("${spring.data.redis.channels.mentorship_requested_channel.name}")
    private String mentorshipRequestedTopic;
    @Value("${spring.data.redis.channels.comment_event.name}")
    private String commentEventTopicName;
    @Value("${spring.data.redis.channels.search_appearance.name}")
    private String searchAppearanceTopic;
    @Value("${spring.data.redis.channels.followers_view}")
    private String followersEventChannel;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Crated redis connection factory with host: {}, port: {}", host, port);
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean
    public MessageListenerAdapter recommendationAdapter(RecommendationEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter postViewAdapter(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestedMessageListener(MentorshipRequestedEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter commentEventAdapter(CommentEventListener commentEventlistener) {
        return new MessageListenerAdapter(commentEventlistener);
    }

    @Bean
    public MessageListenerAdapter searchAppearanceAdapter(SearchAppearanceEventListener searchAppearanceEventListener) {
        return new MessageListenerAdapter(searchAppearanceEventListener);
    }

    @Bean
    public MessageListenerAdapter followerEventAdapter(FollowerEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(searchAppearanceTopic);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewChannel);
    }

    @Bean
    public ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(mentorshipRequestedTopic);
    }

    @Bean
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(commentEventTopicName);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followersEventChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationAdapter,
                                                        MessageListenerAdapter postViewAdapter,
                                                        MessageListenerAdapter mentorshipRequestedMessageListener,
                                                        MessageListenerAdapter commentEventAdapter,
                                                        MessageListenerAdapter searchAppearanceAdapter,
                                                        MessageListenerAdapter followerEventAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationAdapter, recommendationTopic());
        container.addMessageListener(postViewAdapter, postViewTopic());
        container.addMessageListener(mentorshipRequestedMessageListener, mentorshipRequestedTopic());
        container.addMessageListener(commentEventAdapter, commentEventTopic());
        container.addMessageListener(searchAppearanceAdapter, searchAppearanceTopic());
        container.addMessageListener(followerEventAdapter, followerTopic());
        return container;
    }
}