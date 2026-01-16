package faang.school.analytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.ChannelTopic;

import faang.school.analytics.listener.CommentEventListener;
import faang.school.analytics.listener.RecommendationEventListener;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channels.recommendation.name}")
    private String recommendationTopicName;

    @Value("${spring.data.redis.channels.comment.name}")
    private String commentTopicName;

    @Bean
    MessageListenerAdapter recommendationListenerAdapter(RecommendationEventListener eventListener) {
        return new MessageListenerAdapter(eventListener); 
    } 

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationTopicName);
    }

    @Bean
    MessageListenerAdapter commentListenerAdapter(CommentEventListener eventListener) {
        return new MessageListenerAdapter(eventListener); 
    } 

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(commentTopicName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory,
            MessageListenerAdapter recommendationListenerAdapter,
            MessageListenerAdapter commentListenerAdapter
            ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        container.addMessageListener(recommendationListenerAdapter, recommendationTopic());
        container.addMessageListener(commentListenerAdapter, commentTopic());

        return container;
    }
}
