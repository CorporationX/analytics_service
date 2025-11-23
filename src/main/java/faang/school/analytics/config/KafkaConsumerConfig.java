package faang.school.analytics.config;

import faang.school.analytics.dto.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaConsumerProperties consumerProperties;

    @Bean
    public ConsumerFactory<String, ProfileViewEvent> profileViewEventConsumerFactory() {
        return createConsumerFactory(ProfileViewEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileViewEvent> profileViewEventKafkaListenerContainerFactory(
            ConsumerFactory<String, ProfileViewEvent> profileViewEventConsumerFactory) {
        KafkaConsumerProperties.ConsumerConfig config = consumerProperties.getProfileView();
        return createListenerContainerFactory(profileViewEventConsumerFactory, config.getGroupId(), config.getConcurrency());
    }

    @Value("${kafka.consumers.post-view.group-id}")
    private String postViewGroupId;

    @Value("${kafka.consumers.post-view.concurrency}")
    private int postViewConcurrency;

    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> eventClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(eventClass);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createListenerContainerFactory(
            ConsumerFactory<String, T> consumerFactory,
            String groupId,
            int concurrency
    ) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setGroupId(groupId);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProfileViewEvent> profileViewEventConsumerFactory() {
        return createConsumerFactory(profileViewGroupId, ProfileViewEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileViewEvent> profileViewEventKafkaListenerContainerFactory(
            ConsumerFactory<String, ProfileViewEvent> profileViewEventConsumerFactory) {
        return createListenerContainerFactory(profileViewEventConsumerFactory, profileViewConcurrency);
    }

    @Bean("postViewConsumerFactory")
    public ConsumerFactory<String, String> postViewEventConsumerFactory() {
        return createConsumerFactory(postViewGroupId, String.class);
    }

    @Bean("postViewConcurrentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> postViewEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, String> postViewEventConsumerFactory) {
        return createListenerContainerFactory(postViewEventConsumerFactory, postViewConcurrency);
    }
}
