package faang.school.analytics.listener;

import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "project-view")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectViewEventListenerTest {
    @Autowired
    private KafkaTemplate<String, ProjectViewEvent> projectViewEventKafkaTemplate;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Value("${spring.kafka.consumer.project-view.topic}")
    private String projectViewTopic;

    @Test
    void projectViewEventListener_ShouldSaveEvent() throws Exception {
        LocalDateTime timestamp = LocalDateTime.of(2020, 1, 1, 0, 0);
        ProjectViewEvent event = new ProjectViewEvent(2L, 3L, timestamp);

        projectViewEventKafkaTemplate.send(projectViewTopic, event);
        Thread.sleep(3000);

        List<AnalyticsEvent> expected = List.of(AnalyticsEvent.builder()
                .id(1)
                .receiverId(2)
                .actorId(3)
                .eventType(EventType.PROJECT_VIEW)
                .receivedAt(timestamp)
                .build());

        Iterable<AnalyticsEvent> events = analyticsEventRepository.findAll();
        events.forEach(System.out::println);
        List<AnalyticsEvent> result = StreamSupport.stream(events.spliterator(), false)
                .toList();

        assertThat(result)
                .isEqualTo(expected);
    }

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}