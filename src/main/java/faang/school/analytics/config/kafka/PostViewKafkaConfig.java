package faang.school.analytics.config.kafka;

import faang.school.analytics.dto.event.PostViewEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class PostViewKafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value(value = "${kafka.consumer.group-id}")
    private String groupId;

    @Bean(name = "postViewEventFactory")
    public ConsumerFactory<String, PostViewEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(PostViewEvent.class));
    }

    @Bean(name = "postViewEventConcurrentKafkaFactory")
    public ConcurrentKafkaListenerContainerFactory<String, PostViewEvent> kafkaListenerContainerFactory(
            @Qualifier("postViewEventFactory") ConsumerFactory<String, PostViewEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, PostViewEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 5);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(fixedBackOff);

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}