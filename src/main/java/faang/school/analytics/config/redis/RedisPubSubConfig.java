package faang.school.analytics.config.redis;

import faang.school.analytics.service.redis.RedisMessageSubscriber;
import faang.school.analytics.service.redis.listeners.UserEventsListener;
import jakarta.annotation.PostConstruct;
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
  private final UserEventsListener userEventsListener;

  @PostConstruct
  private void postConstruct() {
    System.out.println("DONE");
  }

  @Bean
  public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);

    container.addMessageListener(userEventsListener, new ChannelTopic(userEventsListener.getChannelName()));

    return container;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}

