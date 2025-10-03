package faang.school.analytics.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.config.property.AuditKafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuditKafkaProperties.class)
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> analiticsEventConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(
                consumerProperties,
                new StringDeserializer(),
                new StringDeserializer()
        );
    }

    @Bean
    public KafkaListenerContainerFactory<
                ConcurrentMessageListenerContainer<String, String>> analiticsEventListenerContainerFactory(
            ConsumerFactory<String, String> analiticsEventConsumerFactory,
            ObjectMapper objectMapper
    ) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(analiticsEventConsumerFactory);
        factory.setRecordMessageConverter(new StringJsonMessageConverter(objectMapper));

        return factory;
    }

    @Bean
    public ExponentialBackOffWithMaxRetries expBackOffRetries(AuditKafkaProperties property) {
        ExponentialBackOffWithMaxRetries backOff =
                new ExponentialBackOffWithMaxRetries(property.backoff().maxRetries());
        backOff.setInitialInterval(property.backoff().initInterval());
        backOff.setMaxInterval(property.backoff().maxInterval());
        backOff.setMultiplier(property.backoff().multiplier());

        return backOff;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
