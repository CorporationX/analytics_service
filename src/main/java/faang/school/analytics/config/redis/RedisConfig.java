package faang.school.analytics.config.redis;

import faang.school.analytics.listener.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Value("${spring.data.redis.channel.search-appearance}")
    private String searchAppearanceChannel;

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channel.ad-bought}")
    private String adBoughtChannel;

    @Value("${spring.data.redis.channel.profile-view}")
    private String profileViewChannel;

    @Value("${spring.data.redis.channel.fund-raised}")
    private String fundRaisedChannel;

    @Value("${spring.data.redis.channel.project-view}")
    private String projectViewChannel;

    @Value("${spring.data.redis.channel.premium-bought}")
    private String premiumBoughtChannel;

    @Value("${spring.data.redis.channel.post_view_channel}")
    private String postViewChannel;

    @Value("${spring.data.redis.channel.user-follower}")
    private String userFollowerChannel;

    @Value("${spring.data.redis.channel.project-follower}")
    private String projectFollowerChannel;
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    MessageListenerAdapter likeEvent(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    MessageListenerAdapter recommendationEvent(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    MessageListenerAdapter searchAppearanceEvent(SearchAppearanceEventListener searchAppearanceEventListener) {
        return new MessageListenerAdapter(searchAppearanceEventListener);
    }

    @Bean
    MessageListenerAdapter adBoughtEvent(AdBoughtEventListener adBoughtEventListener) {
        return new MessageListenerAdapter(adBoughtEventListener);
    }

    @Bean
    MessageListenerAdapter profileViewEvent(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    MessageListenerAdapter fundRaisedEvent(FundRaisedEventListener fundRaisedEventListener) {
        return new MessageListenerAdapter(fundRaisedEventListener);
    }

    @Bean
    MessageListenerAdapter premiumBoughtEvent(PremiumBoughtEventListener premiumBoughtEventListener) {
        return new MessageListenerAdapter(premiumBoughtEventListener);
    }

    @Bean
    MessageListenerAdapter projectViewEvent(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    MessageListenerAdapter postViewEvent(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public MessageListenerAdapter userFollowerEvent(UserFollowerEventListener userFollowerEventListener) {
        return new MessageListenerAdapter(userFollowerEventListener);
    }

    @Bean
    public MessageListenerAdapter projectFollowerEvent(ProjectFollowerEventListener projectFollowerEventListener) {
        return new MessageListenerAdapter(projectFollowerEventListener);
    }

    @Bean
    ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(searchAppearanceChannel);
    }

    @Bean
    ChannelTopic adBoughtTopic() {
        return new ChannelTopic(adBoughtChannel);
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewChannel);
    }

    @Bean
    ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewChannel);
    }

    @Bean
    ChannelTopic fundRaisedTopic() {
        return new ChannelTopic(fundRaisedChannel);
    }

    @Bean
    ChannelTopic premiumBoughtTopic() {
        return new ChannelTopic(premiumBoughtChannel);
    }

    @Bean
    ChannelTopic projectViewTopic() {
        return new ChannelTopic(projectViewChannel);
    }

    @Bean
    public ChannelTopic userFollowerEventTopic() {
        return new ChannelTopic(userFollowerChannel);
    }

    @Bean
    public ChannelTopic projectFollowerEventTopic() {
        return new ChannelTopic(projectFollowerChannel);
    }

    @Bean
    public MessageListenerAdapter commentEventListener(CommentCreateListener commentCreateListener) {
        return new MessageListenerAdapter(commentCreateListener);
    }

    @Bean
    public ChannelTopic postCommentChannelTopic() {
        return new ChannelTopic(redisProperties.getChannels().get("post-comment"));
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(LettuceConnectionFactory lettuceConnectionFactory,
                                                        MessageListenerAdapter searchAppearanceEvent,
                                                        MessageListenerAdapter likeEvent,
                                                        MessageListenerAdapter recommendationEvent,
                                                        MessageListenerAdapter adBoughtEvent,
                                                        MessageListenerAdapter profileViewEvent,
                                                        MessageListenerAdapter fundRaisedEvent,
                                                        MessageListenerAdapter premiumBoughtEvent,
                                                        MessageListenerAdapter projectViewEvent,
                                                        MessageListenerAdapter postViewEvent,
                                                        MessageListenerAdapter userFollowerEvent,
                                                        MessageListenerAdapter projectFollowerEvent,
                                                        CommentCreateListener commentCreateListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(likeEvent, likeTopic());
        container.addMessageListener(recommendationEvent, recommendationTopic());
        container.addMessageListener(searchAppearanceEvent, searchAppearanceTopic());
        container.addMessageListener(adBoughtEvent, adBoughtTopic());
        container.addMessageListener(profileViewEvent, profileViewTopic());
        container.addMessageListener(postViewEvent, postViewTopic());
        container.addMessageListener(fundRaisedEvent, fundRaisedTopic());
        container.addMessageListener(premiumBoughtEvent, premiumBoughtTopic());
        container.addMessageListener(projectViewEvent, projectViewTopic());
        container.addMessageListener(userFollowerEvent, userFollowerEventTopic());
        container.addMessageListener(projectFollowerEvent, projectFollowerEventTopic());
        container.addMessageListener(commentEventListener(commentCreateListener), postCommentChannelTopic());
        return container;
    }
}