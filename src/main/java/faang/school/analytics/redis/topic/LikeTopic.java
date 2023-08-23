package faang.school.analytics.redis.topic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class LikeTopic {
    @Value("${spring.data.redis.channel.like_channel}")
    private String likeChannel;

    public ChannelTopic topic() {
        return new ChannelTopic(likeChannel);
    }
}