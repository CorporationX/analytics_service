package faang.school.analytics.integration;

import faang.school.analytics.dto.event.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.PostViewListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@DirtiesContext
@Import(KafkaTestConfiguration.class)
@SpringBootTest
public class AnalyticEventServiceIntegrationTest {

    @Container
    public static KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Container
    public static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testdb")
                    .withUsername("user")
                    .withPassword("password");

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);

        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private KafkaTemplate<String, PostViewEvent> kafkaTemplate;

    @Autowired
    private PostViewListener listener;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    private final PostViewEvent event = new PostViewEvent(1L, 2L, 3L, LocalDateTime.now());

    @BeforeEach
    public void setUp() {
        analyticsEventRepository.deleteAll();
    }


    @Test
    public void testKafkaListenerSavesValidAnalyticsEvent() {
        kafkaTemplate.send("user-post-viewed", event);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            List<AnalyticsEvent> savedEvents = (List<AnalyticsEvent>) analyticsEventRepository.findAll();
            assertThat(savedEvents).isNotEmpty();
            AnalyticsEvent savedEvent = savedEvents.get(0);
            assertThat(savedEvent.getId()).isEqualTo(1L);
            assertThat(savedEvent.getReceiverId()).isEqualTo(2L);
            assertThat(savedEvent.getActorId()).isEqualTo(3L);
        });
    }

    @Test
    public void testKafkaListenerDoesNotSaveInvalidAnalyticsEvent() {
        PostViewEvent badEvent = new PostViewEvent();
        badEvent.setPostId(null);
        badEvent.setUserId(-1L);

        kafkaTemplate.send("user-post-viewed", badEvent);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            List<AnalyticsEvent> savedEvents = (List<AnalyticsEvent>) analyticsEventRepository.findAll();
            assertThat(savedEvents).isEmpty();
        });
    }

    @Test
    public void testKafkaListenerSetInvalidKafkaTopics() {
        kafkaTemplate.send("invalid-topic", event);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            List<AnalyticsEvent> savedEvents = (List<AnalyticsEvent>) analyticsEventRepository.findAll();
            assertThat(savedEvents).isEmpty();
        });
    }
}
