package faang.school.analytics.config;


import faang.school.analytics.dto.analyticsEvent.RecommendationAnalyticDto;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final Environment environment;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RecommendationAnalyticDto> recommendationContainerFactory(
            KafkaProperties kafkaProperties) {
        ConsumerFactory<String, RecommendationAnalyticDto> kafkaProjectViewConsumerFactory =
                getConsumerFactory(kafkaProperties);
        ConcurrentKafkaListenerContainerFactory<String, RecommendationAnalyticDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaProjectViewConsumerFactory);
        return factory;
    }

    private ConsumerFactory<String, RecommendationAnalyticDto> getConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("spring.kafka.consumer.recommendation-create.group-id"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("spring.kafka.consumer.key-deserializer"));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("spring.kafka.consumer.value-deserializer"));

        JsonDeserializer<RecommendationAnalyticDto> valueDeserializer = new JsonDeserializer<>(RecommendationAnalyticDto.class);
        valueDeserializer.addTrustedPackages("*");
        valueDeserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                valueDeserializer);
    }
}

