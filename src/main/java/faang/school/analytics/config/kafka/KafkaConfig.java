package faang.school.analytics.config.kafka;

import faang.school.analytics.dto.ProjectViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final Environment environment;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProjectViewEvent> kafkaProjectViewListenerContainerFactory(
            KafkaProperties kafkaProperties) {
        ConsumerFactory<String, ProjectViewEvent> kafkaProjectViewConsumerFactory = getConsumerFactory(kafkaProperties);
        ConcurrentKafkaListenerContainerFactory<String, ProjectViewEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaProjectViewConsumerFactory);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        log.debug("kafkaProjectViewListenerContainerFactory: {}", factory);
        return factory;
    }

    private ConsumerFactory<String, ProjectViewEvent> getConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("spring.kafka.consumer.project-view.group-id"));
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                environment.getProperty("spring.kafka.consumer.project-view.max.poll.records", Integer.class));
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
                environment.getProperty("spring.kafka.consumer.project-view.max.poll.interval.ms", Integer.class));
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,
                environment.getProperty("spring.kafka.consumer.project-view.fetch.max.wait.ms", Integer.class));
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(ProjectViewEvent.class));
    }
}