package faang.school.analytics.config;

import faang.school.analytics.listener.CommentMessageListener;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.LikePostMessageListener;
import faang.school.analytics.listener.MentorshipMessageListener;
import faang.school.analytics.listener.ProjectViewEventListener;
import lombok.RequiredArgsConstructor;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final LikePostMessageListener likePostMessageListener;
    private final FollowerEventListener followerEventListener;
    private final MessageListener postViewEventListener;
    private final MentorshipMessageListener mentorshipMessageListener;
    private final CommentMessageListener commentMessageListener;
    private final ProjectViewEventListener projectViewEventListener;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.event_channels.likePost}")
    private String likeTopicName;
    @Value("${spring.data.redis.channels.post_channel.name}")
    private String postTopicName;
    @Value("${spring.data.redis.channels.follower_channel.name}")
    private String followerTopicName;
    @Value("${spring.data.redis.channels.event_channels.mentorship}")
    private String mentorshipName;
    @Value("${spring.data.redis.channels.comment_channel.name}")
    private String commentTopicName;
    @Value("${spring.data.redis.channels.project_view_channel.name}")
    private String projectTopicName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        MessageListenerAdapter followerEventMessageListenerAdapter = new MessageListenerAdapter(followerEventListener);
        MessageListenerAdapter likePostMessageListenerAdapter = new MessageListenerAdapter(likePostMessageListener);
        MessageListenerAdapter mentorshipMessageListenerAdapter = new MessageListenerAdapter(mentorshipMessageListener);
        MessageListenerAdapter postMessageListenerAdapter = new MessageListenerAdapter(postViewEventListener);
        MessageListenerAdapter commentMessageListenerAdapter = new MessageListenerAdapter(commentMessageListener);
        MessageListenerAdapter projectMessageListenerAdapter = new MessageListenerAdapter(projectViewEventListener);

        container.addMessageListener(likePostMessageListenerAdapter, new ChannelTopic(likeTopicName));
        container.addMessageListener(followerEventMessageListenerAdapter, new ChannelTopic(followerTopicName));
        container.addMessageListener(mentorshipMessageListenerAdapter, new ChannelTopic(mentorshipName));
        container.addMessageListener(postMessageListenerAdapter, new ChannelTopic(postTopicName));
        container.addMessageListener(commentMessageListenerAdapter, new ChannelTopic(commentTopicName));
        container.addMessageListener(projectMessageListenerAdapter, new ChannelTopic(projectTopicName));

        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}