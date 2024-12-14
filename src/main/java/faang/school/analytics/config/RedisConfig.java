package faang.school.analytics.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final GoalCompletedEventListener goalCompletedEventListener;
    private final RedisProperties redisProperties;


    @Value("${spring.data.redis.channels.goal-completed}")
    private String goalCompletedTopic;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        MentorshipRequestedEventListener listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(goalCompletedEventListener, goalCompletedTopic());
        container.addMessageListener(listener,mentorshipRequestedTopic());
        return container;
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedTopic);
    }

    @Bean
    ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(redisProperties.getChannel().getMentorshipRequest());
    }
}

