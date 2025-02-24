package faang.school.analytics.config.redis;

import faang.school.analytics.listener.comment.CommentCreateEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class CommentMessageListenerConfig {

    @Bean
    MessageListenerAdapter commentCreateMessageListenerAdapter(
            CommentCreateEventListener commentCreateEventListener
    ) {
        return new MessageListenerAdapter(commentCreateEventListener);
    }

    @Bean
    ChannelTopic commentTopic(@Value("${spring.data.redis.channels.comment_create}") String topic) {
        return new ChannelTopic(topic);
    }

}
