package faang.school.analytics.config.redis;

import faang.school.analytics.service.redis.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.context.annotation.Bean;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {
  private final RedisMessageSubscriber messageSubscriber;

  @Bean
  public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);

    // Subscribe to all the channels defined in the RedisMessageSubscriber
    for (String channel : messageSubscriber.getSubscribedChannels()) {
      container.addMessageListener(messageSubscriber, new ChannelTopic(channel));
    }

    return container;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}

