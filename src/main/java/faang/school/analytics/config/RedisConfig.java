package faang.school.analytics.config;

import faang.school.analytics.event.LikeEventListener;
import faang.school.analytics.listener.MentorshipRequestEventListener;
import faang.school.analytics.redisListener.EventListener;
import faang.school.analytics.listener.CommentEventListener;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.ProfileViewEventListener;
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

    private final EventListener<?> followerEventListener;

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.commentСhannel}")
    private String commentEventNameTopic;

    @Value("${spring.data.redis.channel.mentorshipRequest}")
    private String mentorshipRequestTopicName;

    @Value("${spring.data.redis.channel.profileView}")
    private String profileViewTopicName;

    @Value("${spring.data.redis.channel.followerView}")
    private String followerViewTopicName;

    @Value("${spring.data.redis.channel.likePostAnalytics}")
    private String likeTopic;

    private final CommentEventListener commentEventListener;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter FollowerEventListener() {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestListener(MentorshipRequestEventListener mentorshipRequestEventListener) {
        return new MessageListenerAdapter(mentorshipRequestEventListener);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfig =
                new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(standaloneConfig);
    }

    @Bean
    public ChannelTopic followerViewTopic() {
        return new ChannelTopic(followerViewTopicName);
    }

    @Bean
    public ChannelTopic mentorshipRequestTopic() {
        return new ChannelTopic(mentorshipRequestTopicName);
    }

    @Bean
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(commentEventNameTopic);
    }

    public MessageListenerAdapter commentEventListenerAdapter() {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public MessageListenerAdapter likeChannelListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic likeTopicChannel() {
        return new ChannelTopic(likeTopic);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MentorshipRequestEventListener mentorshipRequestListener,
                                                                       LikeEventListener likeEventListener,
                                                                       ProfileViewEventListener profileViewEventListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(jedisConnectionFactory());
        redisMessageListenerContainer.addMessageListener(FollowerEventListener(), followerViewTopic());
        redisMessageListenerContainer.addMessageListener(mentorshipRequestListener, mentorshipRequestTopic());
        redisMessageListenerContainer.addMessageListener(likeChannelListener(likeEventListener), likeTopicChannel());
        redisMessageListenerContainer.addMessageListener(profileViewListener(profileViewEventListener), profileViewTopic());
        redisMessageListenerContainer.addMessageListener(commentEventListenerAdapter(), commentEventTopic());

        return redisMessageListenerContainer;
    }

    @Bean
    public MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    public ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewTopicName);
    }
}