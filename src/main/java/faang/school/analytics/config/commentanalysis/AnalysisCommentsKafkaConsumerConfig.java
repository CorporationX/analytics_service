package faang.school.analytics.config.commentanalysis;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AnalysisCommentsKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Настройки группового ID для потребителя
    @Value("${spring.kafka.group-id}")
    private String groupId;

    @Bean("analyticsConsumerFactory")
    public DefaultKafkaConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // Группа потребителей
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Подсказываем десериализатор, что мы ожидаем объекты типа AnalysisCommentsEventDto
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // Доверяем всем пакетам (для простоты примера)

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }
}