package faang.school.analytics.config;

import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.listner.ProjectViewListener;
import lombok.RequiredArgsConstructor;
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
    @Value("${spring.data.redis.channel.mentorship_request_channel.name}")
    private String mentorshipRequestChannelName;

    @Value("${spring.data.redis.channel.premium_bought.name}")
    private String premiumBoughtChannelName;

    @Value("${spring.data.redis.channel.follower_channel")
    private String followerChannel;

    @Value("${spring.data.redis.channel.project_view.project_view_channel}")
    String profileViewChannelName;

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
    ChannelTopic mentorshipRequestTopic() {
        return new ChannelTopic(mentorshipRequestChannelName);
    }


    @Bean
    MessageListenerAdapter premiumBoughtListenerAdapter(PremiumBoughtEventListener premiumBoughtEventListener) {
        return new MessageListenerAdapter(premiumBoughtEventListener);
    }

    @Bean
    ChannelTopic premiumBoughtTopic() {
        return new ChannelTopic(premiumBoughtChannelName);
    }

    @Bean
    public MessageListenerAdapter followEventListenerAdapter(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
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
    MessageListenerAdapter messageListener(ProjectViewListener projectViewListener) {
        return new MessageListenerAdapter(projectViewListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter premiumBoughtListenerAdapter,
                                                 MessageListenerAdapter mentorshipMessageListenerAdapter,
                                                 MessageListenerAdapter followEventListenerAdapter) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(premiumBoughtListenerAdapter, premiumBoughtTopic());
        container.addMessageListener(mentorshipMessageListenerAdapter, mentorshipRequestTopic());
        container.addMessageListener(followEventListenerAdapter, followEventTopic());
        container.addMessageListener(messageListener, profileViewTopic());
        return container;
    }
}