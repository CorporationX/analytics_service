package faang.school.analytics.config.context.redis;

import faang.school.analytics.listener.MentorshipRequestEventListener;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@AllArgsConstructor
public class RedisConfig {

    @Value("${data.redis.channels.mentorship-request-topic}")
    private String topic;

    @Bean
    public MessageListenerAdapter mentorshipRequestListener(MentorshipRequestEventListener mentorshipRequestEventListener) {
        return new MessageListenerAdapter(mentorshipRequestEventListener); // оборачиваем нашего Listener в MessageListenerAdapter для какой-то там адаптации
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter mentorshipRequestListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory()); //подключение к Redis
        container.addMessageListener(mentorshipRequestListener, mentorshipRequestTopic()); // Регистрация слушателей
        return container;
    }

    @Bean
    public ChannelTopic mentorshipRequestTopic() {
        return new ChannelTopic(topic); // создаем топик с нужным нам названием
    }
}
