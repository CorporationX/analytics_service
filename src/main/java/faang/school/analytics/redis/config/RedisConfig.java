package faang.school.analytics.redis.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.redis.events.PremiumBoughtEventListener;
//import faang.school.analytics.redis.listener.AnalyticsEventListener;
//import lombok.RequiredArgsConstructor;
//import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

@Configuration
@Slf4j

public class RedisConfig {

    private final String premiumBoughtTopicName;

    public RedisConfig(@Value("${spring.data.redis.channel.premium-bought.name}") String premiumBoughtTopicName) {
        this.premiumBoughtTopicName = premiumBoughtTopicName;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic premiumBoughtTopic(@Value("${spring.data.redis.channel.premium-bought.name}") String premiumBoughtTopicName) {
        log.info("Configuring Redis topic: {}", premiumBoughtTopicName);
        return new ChannelTopic(premiumBoughtTopicName);
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            PremiumBoughtEventListener listener,
            ChannelTopic premiumBoughtTopic) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, Collections.singletonList(premiumBoughtTopic));

        log.info("RedisMessageListenerContainer configured for topic: {}", premiumBoughtTopic.getTopic());
        return container;
    }
}



