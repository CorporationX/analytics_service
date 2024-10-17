package faang.school.analytics.config.redis;

import faang.school.analytics.listener.PremiumBoughtEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class PremiumBoughtEventRedisConfig {
    private final ChannelTopic topic;
    private final MessageListenerAdapter adapter;

    public PremiumBoughtEventRedisConfig(
            @Value("${spring.data.redis.channels.premium-channel.name}") String topicName,
            PremiumBoughtEventListener eventListener
    ) {
        this.topic = new ChannelTopic(topicName);
        this.adapter =  new MessageListenerAdapter(eventListener);
    }
    @Bean(name = "premiumChannel")
    public ChannelTopic premiumChannel() {
        return topic;
    }

    @Bean(name = "premiumAdapter")
    public MessageListenerAdapter premiumAdapter() {
        return adapter;
    }
}
