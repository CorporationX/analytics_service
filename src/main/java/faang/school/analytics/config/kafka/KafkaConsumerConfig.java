package faang.school.analytics.config.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AnalyticsEvent> kafkaAnalyticsEventListener() {
        return concurrentKafkaListenerJsonFactory();
    }

    private <T extends AnalyticsEvent> ConcurrentKafkaListenerContainerFactory<String, T> concurrentKafkaListenerJsonFactory() {
        Map<String, Object> jsonFactoryConfig = new HashMap<>();
        jsonFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        jsonFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        jsonFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        jsonFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        jsonFactoryConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();

        ConsumerFactory<String, T> consumerFactory = new DefaultKafkaConsumerFactory<String , T> (
            jsonFactoryConfig,
                    new StringDeserializer(),
                    new JsonDeserializer<>(AnalyticsEvent.class, true)
        );

        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}