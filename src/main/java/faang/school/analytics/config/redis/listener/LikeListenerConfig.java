package faang.school.analytics.config.redis.listener;

import faang.school.analytics.listener.PostLikeEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class LikeListenerConfig {

    @Bean
    public MessageListenerAdapter likeListener(PostLikeEventListener postLikeEventListener) {
        return new MessageListenerAdapter(postLikeEventListener);
    }

    @Bean
    public ChannelTopic likeTopic(@Value("${spring.data.redis.channel.like_topic}") String topicName) {
        return new ChannelTopic(topicName);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> likeListenerChannelPair(
            @Qualifier("postViewListenerAdapter") MessageListenerAdapter adapter,
            @Qualifier("postViewTopic") ChannelTopic channelTopic) {

        return Pair.of(adapter, channelTopic);
    }
}
