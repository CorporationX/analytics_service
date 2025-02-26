package faang.school.analytics.config.kafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;

@EnableKafka
@Configuration
public class PostViewKafkaConfig {

    public ConsumerFactory<String, PostViewEventDto>

}
