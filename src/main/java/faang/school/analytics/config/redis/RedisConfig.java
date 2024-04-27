package faang.school.analytics.config.redis;

import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.listener.postview.PostViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    private final PremiumBoughtEventListener premiumBoughtEventListener;
    @Value("${spring.data.redis.channel.post-view}")
    private String postViewTopic;

    private final PostViewEventListener postViewListener;

    @Bean
    ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewTopic);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public MessageListenerAdapter premiumBoughtListenerAdapter() {
        return new MessageListenerAdapter(premiumBoughtEventListener);
    }

    @Bean
    public MessageListenerAdapter postViewListenerAdapter() {
        return new MessageListenerAdapter(postViewListener);
    }

    @Bean
    public RedisMessageListenerContainer premiumBoughtMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(premiumBoughtListenerAdapter(), premiumBoughtTopic());
        container.addMessageListener(postViewListenerAdapter(), postViewTopic());
        return container;
    }

    @Bean
    public ChannelTopic premiumBoughtTopic() {
        return new ChannelTopic("premium_bought");
    }
}
