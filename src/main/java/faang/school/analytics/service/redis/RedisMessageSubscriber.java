package faang.school.analytics.service.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RedisMessageSubscriber implements MessageListener {
  private List<String> subscribedChannels = Arrays.asList("analytics_topic");

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String channel = new String(message.getChannel());
    String body = new String(message.getBody());

    System.out.println("Received message: " + body + " from channel: " + channel);
  }

  public void subscribe(String channel) {
    if (!subscribedChannels.contains(channel)) {
      subscribedChannels.add(channel);
    }
  }

  public void unsubscribe(String channel) {
    subscribedChannels.remove(channel);
  }

  public List<String> getSubscribedChannels() {
    return subscribedChannels;
  }
}