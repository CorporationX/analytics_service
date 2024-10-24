package faang.school.analytics.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RedisProperties {
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.channels.post-service.user-ban-channel.name}")
    private String userBanChannelName;

    @Value("${spring.data.redis.channels.post-service.comment-channel.name}")
    private String commentEventChannelName;

    @Value("${spring.data.redis.channels.post-service.post-like-channel.name}")
    private String postLikeEventChannelName;

    @Value("${spring.data.redis.channel.comment-event}")
    private String commentEventTopicName;

}
