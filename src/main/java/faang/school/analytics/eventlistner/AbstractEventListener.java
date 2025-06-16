package faang.school.analytics.eventlistner;

import faang.school.analytics.config.redis.RedisProperties;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractEventListener implements MessageListener {
    public abstract Set<ChannelTopic> getChannelTopics();

    public Set<ChannelTopic> getChanelTopics(List<String> topicNames, RedisProperties properties) {
        return properties.getChannel().entrySet().stream()
                .filter(entry -> topicNames.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(ChannelTopic::new)
                .collect(Collectors.toSet());
    }
}
