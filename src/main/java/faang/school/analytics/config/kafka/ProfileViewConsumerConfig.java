package faang.school.analytics.config.kafka;

import faang.school.analytics.messaging.event.ProfileViewEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class ProfileViewConsumerConfig extends KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, ProfileViewEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(defineProps(), new StringDeserializer(),
                new JsonDeserializer<>(ProfileViewEvent.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileViewEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProfileViewEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
