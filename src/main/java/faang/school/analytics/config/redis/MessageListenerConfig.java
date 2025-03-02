package faang.school.analytics.config.redis;

import faang.school.analytics.listener.comment.CommentCreateEventListener;
import faang.school.analytics.messaging.LikeEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class MessageListenerConfig {

    @Value("${spring.data.redis.channels.comment_create}")
    private String commentCreateTopic;
    @Value("${spring.data.redis.channels.likes}")
    private String likesTopic;

    @Bean
    MessageListenerAdapter commentCreateMessageListenerAdapter(
            CommentCreateEventListener commentCreateEventListener
    ) {
        return new MessageListenerAdapter(commentCreateEventListener);
    }

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(commentCreateTopic);
    }

    @Bean
    MessageListenerAdapter likeListenerAdapter(LikeEventListener likesEventListener) {
        return new MessageListenerAdapter(likesEventListener);
    }

    @Bean
    ChannelTopic likesTopic() {
        return new ChannelTopic(likesTopic);
    }

}
