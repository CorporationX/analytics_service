package faang.school.analytics.config.redis.postview;

import faang.school.analytics.messaging.listener.postview.PostViewEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class PostViewConfig {
    @Value("{spring.data.redis.channels.post_view}")
    private String postViewChannel;

    @Bean
    MessageListenerAdapter postViewListener(PostViewEventListener postViewListener) {
        return new MessageListenerAdapter(postViewListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> postView(
            @Qualifier("postViewListener") MessageListenerAdapter postViewListener) {
        return Pair.of(postViewListener, new ChannelTopic(postViewChannel));
    }
}