package faang.school.analytics.config.messagebroker;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.exception.NonRetryableException;
import faang.school.analytics.exception.RetryableException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumer {

    @Value("${spring.kafka.bootstrap.server.address}")
    private String bootstrapAddress;
    @Value("${spring.kafka.consumer.retry-interval-ms}")
    private Long retryIntervalMillis;
    @Value("${spring.kafka.consumer.max-attempts}")
    private Long retryMaxAttempts;

    @Bean("likeConsumerFactory")
    public ConsumerFactory<String, LikeEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LikeEvent.class);
        props.put(JsonDeserializer.TYPE_MAPPINGS, "LikeEvent:faang.school.analytics.event.LikeEvent");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("likeKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, LikeEvent> kafkaListenerContainerFactory(
            @Qualifier("likeConsumerFactory") ConsumerFactory<String, LikeEvent> consumerFactory,
            DefaultErrorHandler kafkaErrorHandler) {

        ConcurrentKafkaListenerContainerFactory<String, LikeEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        return factory;
    }

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(@Qualifier("dltKafkaTemplate") KafkaTemplate<String,
            Object> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        FixedBackOff backOff = new FixedBackOff(retryIntervalMillis, retryMaxAttempts);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        errorHandler.addNotRetryableExceptions(NonRetryableException.class);
        errorHandler.addNotRetryableExceptions(RecordDeserializationException.class);
        errorHandler.addRetryableExceptions(RetryableException.class);

        return errorHandler;
    }
}
