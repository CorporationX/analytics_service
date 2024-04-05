package faang.school.analytics.config;

import faang.school.analytics.dto.event.SearchAppearanceEventDto;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.listener.ProjectViewListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.channel.mentorship_offered_channel.name}")
    private String mentorshipOfferedChannelName;

    @Value("${spring.data.redis.channel.premium_bought.name}")
    private String premiumBoughtChannelName;

    @Value("${spring.data.redis.channel.follower_channel")
    private String followerChannel;

    @Value("${spring.data.redis.channel.project_view_channel.name}")
    String profileViewChannelName;

    @Value("${spring.data.redis.channel.search_appearance_channel.name}")
    private String searchAppearanceChannel;


    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    MessageListenerAdapter mentorshipMessageListenerAdapter(
            MentorshipRequestedEventListener mentorshipRequestedEventListener) {
        return new MessageListenerAdapter(mentorshipRequestedEventListener);
    }

    @Bean
    MessageListenerAdapter premiumBoughtListenerAdapter(PremiumBoughtEventListener premiumBoughtEventListener) {
        return new MessageListenerAdapter(premiumBoughtEventListener);
    }

    @Bean
    public MessageListenerAdapter followEventListenerAdapter(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter projectViewListenerAdapter(ProjectViewListener projectViewListener) {
        return new MessageListenerAdapter(projectViewListener);
    }

    @Bean
    MessageListenerAdapter searchAppearanceEventListenerAdapter(SearchAppearanceEventListener searchAppearanceEventListener) {
        return new MessageListenerAdapter(searchAppearanceEventListener);
    }

    @Bean
    ChannelTopic mentorshipOfferedTopic() {
        return new ChannelTopic(mentorshipOfferedChannelName);
    }

    @Bean
    ChannelTopic premiumBoughtTopic() {
        return new ChannelTopic(premiumBoughtChannelName);
    }

    @Bean
    public ChannelTopic followEventTopic() {
        return new ChannelTopic(followerChannel);
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewChannelName);
    }

    @Bean
    ChannelTopic searchAppearanceEventTopic() {
        return new ChannelTopic(searchAppearanceChannel);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter premiumBoughtListenerAdapter,
                                                 MessageListenerAdapter mentorshipMessageListenerAdapter,
                                                 MessageListenerAdapter followEventListenerAdapter,
                                                 MessageListenerAdapter projectViewListenerAdapter,
                                                 MessageListenerAdapter searchAppearanceEventListenerAdapter) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(premiumBoughtListenerAdapter, premiumBoughtTopic());
        container.addMessageListener(mentorshipMessageListenerAdapter, mentorshipOfferedTopic());
        container.addMessageListener(followEventListenerAdapter, followEventTopic());
        container.addMessageListener(projectViewListenerAdapter, profileViewTopic());
        container.addMessageListener(searchAppearanceEventListenerAdapter, searchAppearanceEventTopic());
        return container;
    }
}