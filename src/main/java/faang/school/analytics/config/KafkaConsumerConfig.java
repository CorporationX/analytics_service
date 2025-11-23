package faang.school.analytics.config;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.dto.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaConsumerProperties consumerProperties;

    @Bean
    public ConsumerFactory<String, ProfileViewEvent> profileViewEventConsumerFactory() {
        KafkaConsumerProperties.ConsumerConfig config = consumerProperties.getProfileView();
        return createConsumerFactory(config.getGroupId(), ProfileViewEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileViewEvent> profileViewEventKafkaListenerContainerFactory(
            ConsumerFactory<String, ProfileViewEvent> profileViewEventConsumerFactory) {
        KafkaConsumerProperties.ConsumerConfig config = consumerProperties.getProfileView();
        return createListenerContainerFactory(profileViewEventConsumerFactory, config.getConcurrency());
    }

    @Bean("postViewConsumerFactory")
    public ConsumerFactory<String, PostViewEvent> postViewEventConsumerFactory() {
        KafkaConsumerProperties.ConsumerConfig config = consumerProperties.getPostView();
        return createConsumerFactory(config.getGroupId(), PostViewEvent.class);
    }

    @Bean("postViewConcurrentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, PostViewEvent> postViewEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, PostViewEvent> postViewEventConsumerFactory) {
        KafkaConsumerProperties.ConsumerConfig config = consumerProperties.getPostView();
        return createListenerContainerFactory(postViewEventConsumerFactory, config.getConcurrency());
    }

    private <T> ConsumerFactory<String, T> createConsumerFactory(String groupId, Class<T> eventClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(eventClass);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createListenerContainerFactory(
            ConsumerFactory<String, T> consumerFactory,
            int concurrency
    ) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}