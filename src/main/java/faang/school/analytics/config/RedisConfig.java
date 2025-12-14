package faang.school.analytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.ChannelTopic;

import faang.school.analytics.listener.RecommendationEventListener;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channels.recommendation.name}")
    private String recommendationTopicName;

    @Bean
    MessageListenerAdapter recommendationListenerAdapter(RecommendationEventListener eventListener) {
        return new MessageListenerAdapter(eventListener); 
    } 

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationTopicName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory,
            MessageListenerAdapter recommendationListenerAdapter
            ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        container.addMessageListener(recommendationListenerAdapter, recommendationTopic());

        return container;
    }
}
