package faang.school.analytics.config;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.dto.ProfileViewEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
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
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumers.profile-view.group-id}")
    private String profileViewGroupId;

    @Value("${kafka.consumers.profile-view.concurrency}")
    private int profileViewConcurrency;

    @Value("${kafka.consumers.post-view.group-id}")
    private String postViewGroupId;

    @Value("${kafka.consumers.post-view.concurrency}")
    private int postViewConcurrency;

    private Map<String, Object> commonConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    private <T> ConsumerFactory<String, T> createConsumerFactory(String groupId, Class<T> valueType) {
        Map<String, Object> props = commonConsumerConfigs();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueType.getName());
        return new DefaultKafkaConsumerFactory<>(props);
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
    public ConsumerFactory<String, PostViewEvent> postViewEventConsumerFactory() {
        return createConsumerFactory(postViewGroupId, PostViewEvent.class);
    }

    @Bean("postViewConcurrentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, PostViewEvent> postViewEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, PostViewEvent> postViewEventConsumerFactory) {
        return createListenerContainerFactory(postViewEventConsumerFactory, postViewConcurrency);
    }
}
