package faang.school.analytics.config;

import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listener.MentorshipRequestsEventListener;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@AllArgsConstructor
public class RedisConfiguration {
    private final RedisProperties redisProperties;
    private final LikeEventListener likeEventListener;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public ChannelTopic mentorshipRequestsTopic() {
        return new ChannelTopic(redisProperties.getChannel().getMentorshipRequests());
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestsListener(
            MentorshipRequestsEventListener mentorshipRequestsEventListener) {
        return new MessageListenerAdapter(mentorshipRequestsEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            MessageListenerAdapter mentorshipRequestsListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());

        container.addMessageListener(mentorshipRequestsListener, mentorshipRequestsTopic());
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
