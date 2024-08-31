package faang.school.analytics.config.context;

import faang.school.analytics.listener.FollowerEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import faang.school.analytics.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {
    private final LikeEventListener likeEventListener;
    private final FollowerEventListener followerEventListener;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.follow}")
    private String followerViewChannelName;
    @Value("${spring.data.redis.channel.like}")
    private String likeTopicName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }
  
    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener){
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerViewChannelName);
    }
  
    @Bean
    ChannelTopic likeTopic(){
        return new ChannelTopic(likeTopicName);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(likeListener(likeEventListener), likeTopic());
        container.addMessageListener(followerListener(followerEventListener), followerTopic());
        return container;
    }

}
