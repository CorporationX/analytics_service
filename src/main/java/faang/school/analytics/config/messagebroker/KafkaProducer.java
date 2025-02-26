package faang.school.analytics.config.messagebroker;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class KafkaProducer {
    @Value("${spring.kafka.bootstrap.server.address}")
    private String bootstrapAddress;

    @Bean("dltProducerFactory")
    public ProducerFactory<String, Object> dltProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean("dltKafkaTemplate")
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(dltProducerFactory());
    }
}
