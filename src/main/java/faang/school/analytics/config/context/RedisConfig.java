package faang.school.analytics.config.context;

import faang.school.analytics.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class RedisConfig {

    private final LikeEventListener likeEventListener;

    @Value("${spring.data.redis.channel.like}")
    private String likeTopicName;

    @Autowired
    public RedisConfig(LikeEventListener likeEventListener) {
        this.likeEventListener = likeEventListener;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(likeListener(likeEventListener), likeTopic());

        return container;
    }

    @Bean
    ChannelTopic likeTopic(){
        return new ChannelTopic(likeTopicName);
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener){
        return new MessageListenerAdapter(likeEventListener);
    }
}
