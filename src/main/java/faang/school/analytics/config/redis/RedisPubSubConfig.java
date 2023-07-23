package faang.school.analytics.config.redis;

import faang.school.analytics.service.redis.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.context.annotation.Bean;

@Configuration
public class RedisPubSubConfig {
  private static String  CHANNEL_TOPIC_NAME = "analytics_topic";

  private final RedisMessageSubscriber messageSubscriber;

  @Autowired
  public RedisPubSubConfig(RedisMessageSubscriber messageSubscriber) {
    this.messageSubscriber = messageSubscriber;
  }

  @Bean
  public ChannelTopic channelTopic() {
    return new ChannelTopic(CHANNEL_TOPIC_NAME);
  }

  @Bean
  public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);
    container.addMessageListener(messageSubscriber, channelTopic());
    return container;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}

