package faang.school.analytics.consumer.postviewevent;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        topics = {"post-view"},
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        })
@Disabled
class PostViewEventConsumerTest {

    private static final String TOPIC = "post-view";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Mock
    private AnalyticsEventService service;

    @BeforeEach
    void setUp() {
        Mockito.lenient().doNothing().when(service).savePostEvent(Mockito.any(PostViewEvent.class));
    }

    @Test
    @Disabled
    void sendMessage() throws Exception {
        PostViewEvent toConsume = new PostViewEvent(1L, 1L, null, 1L);

        Producer<String, PostViewEvent> producer = configureProducer();

        producer.send(new ProducerRecord<>(TOPIC, toConsume));
    }

    private Producer<String, PostViewEvent> configureProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker.getBrokersAsString());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaProducer(producerProps, new StringSerializer(), new JsonSerializer<PostViewEvent>());
    }
}

