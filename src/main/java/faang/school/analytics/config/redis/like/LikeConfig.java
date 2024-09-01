package faang.school.analytics.config.redis.like;

import faang.school.analytics.messaging.listener.like.LikeEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class LikeConfig {

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> likedPost(
            @Qualifier("likeListener") MessageListenerAdapter likeListener) {
        return Pair.of(likeListener, new ChannelTopic(likeChannel));
    }
}
