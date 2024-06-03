package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.CompletedGoalListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class CompletedGoalRedisConfig {

    @Bean
    public Pair<Topic, MessageListenerAdapter> getCompleteGoalListenerAdapterPair(
            @Value("${spring.data.redis.channel.goal-complete.name}") String channelTopicName,
            @Qualifier("completedGoalMessageAdapter") MessageListenerAdapter messageListenerAdapter) {

        return Pair.of(new ChannelTopic(channelTopicName), messageListenerAdapter);
    }

    @Bean
    public MessageListenerAdapter completedGoalMessageAdapter(@Qualifier("completedGoalMessageListener") MessageListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListener completedGoalMessageListener(ObjectMapper objectMapper) {
        return new CompletedGoalListener(objectMapper);
    }
}
