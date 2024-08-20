package faang.school.analytics.config.context.redis;

import faang.school.analytics.subscruber.GoalEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class GoalConfig {
    @Value("${spring.data.redis.goal_channel.name}")
    private String goalTopic;

    @Bean
    public ChannelTopic goalTopic() {
        return new ChannelTopic(goalTopic);
    }

    @Bean
    MessageListenerAdapter followerEventListener(GoalEventListener goalEventListener) {
        return new MessageListenerAdapter(goalEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> followerRequester(MessageListenerAdapter followerEventListener) {
        return Pair.of(followerEventListener, goalTopic());
    }
}
