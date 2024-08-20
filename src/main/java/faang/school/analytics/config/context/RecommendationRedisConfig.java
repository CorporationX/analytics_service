package faang.school.analytics.config.context;

import faang.school.analytics.listeners.RecommendationListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class RecommendationRedisConfig {

    @Value("${spring.data.redis.channel.recommendation_view}")
    private String recommendationTopic;

    @Bean
    public MessageListenerAdapter recommendationListenerAdapter(RecommendationListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> recommendationListenerAdapterPair(
            @Qualifier("recommendationListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(recommendationTopic), messageListenerAdapter);
    }
}