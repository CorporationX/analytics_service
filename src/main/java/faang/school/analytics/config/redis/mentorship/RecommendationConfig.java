package faang.school.analytics.config.redis.mentorship;

import faang.school.analytics.listener.RecommendationEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class RecommendationConfig {
    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    @Bean
    MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> recommendation(
            @Qualifier("recommendationListener") MessageListenerAdapter recommendationListener) {
        return Pair.of(recommendationListener, new ChannelTopic(recommendationChannel));
    }
}
