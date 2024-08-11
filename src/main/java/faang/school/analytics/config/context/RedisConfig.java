package faang.school.analytics.config.context;

import faang.school.analytics.subscruber.GoalEventSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.goal_channel.name}")
    private String goalTopicChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host,port);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    ChannelTopic goalTopic() {
        return new ChannelTopic(goalTopicChannel);
    }

    @Bean
    MessageListenerAdapter goalEventListener(GoalEventSubscriber goalEventSubscriber){
        return new MessageListenerAdapter(goalEventSubscriber);
    }
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(JedisConnectionFactory jedisConnectionFactory,
                                                                       MessageListenerAdapter goalEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(goalEventListener, goalTopic());
        return container;
    }


}
