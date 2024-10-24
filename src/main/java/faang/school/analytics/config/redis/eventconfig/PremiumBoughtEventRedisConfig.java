package faang.school.analytics.config.redis.eventconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class PremiumBoughtEventRedisConfig extends AbstractEventRedisConfig {

    public PremiumBoughtEventRedisConfig(
            @Value("${spring.data.redis.channels.premium-channel.name}") String topicName,
            @Qualifier("premiumBoughtEventListener") MessageListener eventListener
    ) {
        super(topicName, eventListener);
    }
    @Bean(name = "premiumChannel")
    @Override
    public ChannelTopic getTopic() {
        return topic;
    }

    @Bean(name = "premiumAdapter")
    @Override
    public MessageListenerAdapter getAdapter() {
        return adapter;
    }
}
