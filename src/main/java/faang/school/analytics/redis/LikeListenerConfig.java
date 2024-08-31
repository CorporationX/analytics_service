package faang.school.analytics.redis;

import faang.school.analytics.listener.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
@RequiredArgsConstructor
public class LikeListenerConfig {

    @Value("${spring.data.redis.channel.like_event_channel.name}")
    private String likeEventChannel;

    @Bean
    public MessageListenerAdapter likeListenerAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> like(@Qualifier("likeListenerAdapter")MessageListenerAdapter adapter){
        return Pair.of(adapter, new ChannelTopic(likeEventChannel));
    }
}
