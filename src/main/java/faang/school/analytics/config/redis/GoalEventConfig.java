package faang.school.analytics.config.context.redis;

import faang.school.analytics.subscruber.GoalEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class GoalEventConfig {
    @Value("${spring.data.redis.channel.goal}")
    private String goalTopic;

    @Bean
    public ChannelTopic goalTopic() {
        return new ChannelTopic(goalTopic);
    }

    @Bean
    MessageListenerAdapter goalEventAdapter(GoalEventListener goalEventListener) {
        return new MessageListenerAdapter(goalEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> goalRequester(MessageListenerAdapter goalEventAdapter) {
        return Pair.of(goalEventAdapter, goalTopic());
    }
}
