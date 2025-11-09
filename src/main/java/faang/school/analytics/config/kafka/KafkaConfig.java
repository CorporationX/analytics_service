package faang.school.analytics.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Value("${spring.data.kafka.bootstrap_servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, Object> objectConsumerFactory() {
        Map<String, Object> configProperties = new HashMap<>();
        JsonDeserializer<Object> deserializer =
                new JsonDeserializer<>(Object.class, false);
        deserializer.addTrustedPackages("*");
        configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        //configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "goal-completed-group");
        configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProperties, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> objectContainerFactory(
            ConsumerFactory<String, Object> objectConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> container =
                new ConcurrentKafkaListenerContainerFactory<>();
        container.setConcurrency(1);
        container.setConsumerFactory(objectConsumerFactory);
        return container;
    }
}
