package faang.school.analytics.config.kafka;

import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.FundRaisedEvent;
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
        ConsumerFactory<String, ProjectViewEvent> kafkaProjectViewConsumerFactory = getProjectViewConsumerFactory(kafkaProperties);
        ConcurrentKafkaListenerContainerFactory<String, ProjectViewEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaProjectViewConsumerFactory);
        log.debug("kafkaProjectViewListenerContainerFactory: {}", factory);
        return factory;
    }

    private ConsumerFactory<String, ProjectViewEvent> getProjectViewConsumerFactory(KafkaProperties kafkaProperties) {
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FundRaisedEvent> kafkaFundRaisedListenerContainerFactory(
            KafkaProperties kafkaProperties) {
        ConsumerFactory<String, FundRaisedEvent> kafkaFundRaisedConsumerFactory =
                getFundRaisedConsumerFactory(kafkaProperties);
        ConcurrentKafkaListenerContainerFactory<String, FundRaisedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaFundRaisedConsumerFactory);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private ConsumerFactory<String, ProjectViewEvent> getConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("spring.kafka.consumer.project-view.group-id"));
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(ProjectViewEvent.class));
    }

    private ConsumerFactory<String, FundRaisedEvent> getFundRaisedConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("spring.kafka.consumer.fund-raised.group-id"));
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                environment.getProperty("spring.kafka.consumer.fund-raised.max.poll.records", Integer.class));
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
                environment.getProperty("spring.kafka.consumer.fund-raised.max.poll.interval.ms", Integer.class));
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,
                environment.getProperty("spring.kafka.consumer.fund-raised.fetch.max.wait.ms", Integer.class));
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(FundRaisedEvent.class));
    }
}