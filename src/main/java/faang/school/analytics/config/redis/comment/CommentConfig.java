package faang.school.analytics.config.redis.comment;

import faang.school.analytics.messaging.listener.comment.CommentEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class CommentConfig {

  @Value("${spring.data.redis.channel.comment-chanel}")
  private String commentChannel;

  @Bean
  MessageListenerAdapter commentListener(
      CommentEventListener commentEventListener) {
    return new MessageListenerAdapter(commentEventListener);
  }

  @Bean
  Pair<MessageListenerAdapter, ChannelTopic> recommendationRequested(
      @Qualifier("commentListener") MessageListenerAdapter messageListenerAdapter) {
    return Pair.of(messageListenerAdapter, new ChannelTopic(commentChannel));
  }

}
