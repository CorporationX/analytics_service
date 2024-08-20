package faang.school.analytics.config.redis.listener;

import faang.school.analytics.redis.lisener.PostViewedEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class PostViewListenerConfig {

    @Bean
    public MessageListenerAdapter postViewListenerAdapter(PostViewedEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public ChannelTopic postViewTopic(@Value("${spring.data.redis.channel.post_view_event_channel}") String topicName) {
        return new ChannelTopic(topicName);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> postViewListenerChannelPair(
            @Qualifier("postViewListenerAdapter") MessageListenerAdapter adapter,
            @Qualifier("postViewTopic") ChannelTopic channelTopic) {

        return Pair.of(adapter, channelTopic);
    }
}
