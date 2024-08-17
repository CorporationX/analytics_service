package faang.school.analytics.config.redis.listener;

import faang.school.analytics.redis.lisener.CommentEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@RequiredArgsConstructor
@Configuration
public class CommentEventConfig {

    @Value("${spring.data.redis.channel.post_comment_channel}")
    private String channelTopic;

    @Bean
    public MessageListenerAdapter commentEventListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(channelTopic);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> commentListenerChannelPair(
            @Qualifier("commentEventListenerAdapter") MessageListenerAdapter adapter,
            @Qualifier("commentTopic") ChannelTopic channelTopic) {
        return Pair.of(adapter, channelTopic);
    }
}