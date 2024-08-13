package faang.school.analytics.config.redis;

import faang.school.analytics.redis.lisener.CommentEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@RequiredArgsConstructor
@Configuration
public class CommentEventConfig {

    private final CommentEventListener commentEventListener;

    @Value("${spring.data.redis.channel.post_comment_channel}")
    private String channelTopic;

    @Bean
    public MessageListenerAdapter commentEventListenerAdapter() {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(channelTopic);
    }
}
