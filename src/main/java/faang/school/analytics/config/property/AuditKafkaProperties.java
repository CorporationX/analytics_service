package faang.school.analytics.config.property;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "audit-kafka")
public record AuditKafkaProperties(
        String topic,
        ConsumerProperty consumer,
        @NestedConfigurationProperty
        BackOffProperty backoff,
        @NotNull
        Boolean enabled
) {
}
