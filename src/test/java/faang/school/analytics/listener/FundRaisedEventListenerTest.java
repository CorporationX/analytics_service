package faang.school.analytics.listener;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.FundRaisedEvent;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 1, topics = "fund_raised")
public class FundRaisedEventListenerTest {
    @Autowired
    private KafkaTemplate<String, FundRaisedEvent> fundRaisedEventKafkaTemplate;
    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;
    @Value("${spring.kafka.consumer.fund-raised.topic}")
    private String fundRaisedTopic;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("testdb")
            .withUsername("testUser")
            .withPassword("testPassword");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testFundRaisedEventListener_ShouldSaveEvent() throws InterruptedException {
        BigDecimal amount = new BigDecimal(100);
        LocalDateTime donationTime = LocalDateTime.of(2024, 2, 9, 12, 0);

        FundRaisedEvent event = new FundRaisedEvent(1L, 1L, amount, donationTime);
        fundRaisedEventKafkaTemplate.send(fundRaisedTopic, event);

        Thread.sleep(3000);

        List<AnalyticsEvent> expected = List.of(AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.FUND_RAISED)
                .receivedAt(donationTime)
                .build());

        List<AnalyticsEvent> actual = StreamSupport.stream(
                analyticsEventRepository.findAll().spliterator(), false)
                .toList();

        assertEquals(expected, actual);
    }
}
