package faang.school.analytics.config.redis;

import faang.school.analytics.listener.MentorshipRequestListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfigMentorshipRequest {

    @Value("${spring.data.redis.mentorship-channel.name}")
    private String topicName;

    @Bean
    public MessageListenerAdapter messageListener(MentorshipRequestListener consumerMentorshipRequests) {
        return new MessageListenerAdapter(consumerMentorshipRequests);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MentorshipRequestListener consumerMentorshipRequests) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = messageListener(consumerMentorshipRequests);
        container.addMessageListener(messageListenerAdapter, topic());

        return container;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(topicName);
    }
}
