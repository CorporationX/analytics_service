package faang.school.analytics.config.redis;

import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.listener.AdBoughtEventListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import faang.school.analytics.listener.donation_analysis.FundRaisedEventListener;
import faang.school.analytics.listener.profileView.ProfileViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;
    private final List<AbstractEventListener<?>> eventListeners;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(redisProperties.host(), redisProperties.port());
        return new JedisConnectionFactory(configuration);
    }

    @Value("${spring.data.redis.channels.name}")
    private String fundRaisedTopic;

    @Value("${spring.data.redis.channel.search-appearance-channel.name}")
    private String searchAppearanceTopic;

    @Value("${spring.data.redis.channel.ad-bought-channel.name}")
    private String adBoughtEvent;

    @Value("${spring.data.redis.channel.profileView-channel.name}")
    private String profileViewEventTopic;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            SearchAppearanceEventListener searchAppearanceEventListener,
            MessageListenerAdapter fundRaisedListener,
            MessageListenerAdapter adBoughtListener,
            MessageListenerAdapter profileViewListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        eventListeners.forEach(listener ->
                container.addMessageListener(listener.getListenerAdapter(), listener.getChannelTopic()));
        container.addMessageListener(fundRaisedListener, topic());
        container.addMessageListener(searchAppearanceEventListener, searchAppearanceTopic());
        container.addMessageListener(adBoughtListener, adBoughtTopic());
        container.addMessageListener(profileViewListener,profileViewTopic());
        return container;
    }

    @Bean
    MessageListenerAdapter fundRaisedListener(FundRaisedEventListener fundRaisedEventListener) {
        return new MessageListenerAdapter(fundRaisedEventListener);
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(fundRaisedTopic);
    }

    @Bean
    MessageListenerAdapter searchAppearanceListener(SearchAppearanceEventListener searchAppearanceEventListener) {
        return new MessageListenerAdapter(searchAppearanceEventListener);
    }

    @Bean
    public ChannelTopic searchAppearanceTopic() {
        return new ChannelTopic(searchAppearanceTopic);
    }

    @Bean
    MessageListenerAdapter adBoughtListener(AdBoughtEventListener adBoughtEventListener) {
        return new MessageListenerAdapter(adBoughtEventListener);
    }

    @Bean
    ChannelTopic adBoughtTopic() {
        return new ChannelTopic(adBoughtEvent);
    }

    @Bean
    MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewEventTopic);
    }
}