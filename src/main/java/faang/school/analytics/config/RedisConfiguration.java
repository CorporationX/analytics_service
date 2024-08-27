package faang.school.analytics.config;

import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.MentorshipRequestsEventListener;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@AllArgsConstructor
public class RedisConfiguration {
    private final RedisProperties redisProperties;
    private final MentorshipRequestsEventListener mentorshipRequestsEventListener;
    private final LikeEventListener likeEventListener;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        return new JedisConnectionFactory(config);
    }

    @Bean
    public ChannelTopic mentorshipRequestsTopic() {
        return new ChannelTopic(redisProperties.getChannel().getMentorshipRequests());
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestsListener() {
        return new MessageListenerAdapter(mentorshipRequestsEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        container.addMessageListener(mentorshipRequestsListener(), mentorshipRequestsTopic());
        container.addMessageListener(likeListener(likeEventListener), likeTopic());

        return container;
    }

    @Bean
    ChannelTopic likeTopic(){
        return new ChannelTopic(redisProperties.getChannel().getLike());
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener){
        return new MessageListenerAdapter(likeEventListener);
    }
}
