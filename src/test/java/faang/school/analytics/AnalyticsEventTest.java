package faang.school.analytics;

import faang.school.analytics.listener.AnalyticsEventListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(topics = {"testKafka"}, partitions = 1)
public class AnalyticsEventTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private AnalyticsEventListener analyticsEventListener;

    @Test
    public void checkConsumer() throws IOException {
        String resource = getResource("json/analyticsEvent.json");
        kafkaTemplate.send("testKafka", resource);
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                        .untilAsserted(() -> {
                            Mockito.verify(analyticsEventListener, Mockito.atLeastOnce());
                        });
    }

    private String getResource(String path) throws IOException {
        return Files.readString(new ClassPathResource(path).getFile().toPath());
    }
}
