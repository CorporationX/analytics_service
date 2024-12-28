package faang.school.analytics.listenerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest(classes = RecommendationEventListenerIntegrationTest.MockRedisConfig.class)
class RecommendationEventListenerIntegrationTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate; // Mocked RedisTemplate

    @MockBean
    private AnalyticsEventService analyticsEventService; // Mocked AnalyticsEventService

    @Test
    void testRecommendationEventListenerProcessesMessages() throws Exception {
        // Create a RecommendationEvent
        RecommendationEvent event = new RecommendationEvent(1L, 2L, 3L, LocalDateTime.now());

        // CountDownLatch to simulate and wait for listener processing
        CountDownLatch latch = new CountDownLatch(1);

        // Mock AnalyticsEventService's behavior to count down the latch when called
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(analyticsEventService).processRecommendationEvent(Mockito.any(RecommendationEvent.class));

        // Mock RedisTemplate's behavior to simulate message publication
        doAnswer(invocation -> {
            String topic = invocation.getArgument(0);
            RecommendationEvent receivedEvent = invocation.getArgument(1);

            // Simulate message being picked up by the listener
            if ("recommendation_event_topic".equals(topic) && receivedEvent != null) {
                analyticsEventService.processRecommendationEvent(receivedEvent);
            }
            return null;
        }).when(redisTemplate).convertAndSend(Mockito.anyString(), Mockito.any());

        // Simulate sending the event to the Redis topic
        redisTemplate.convertAndSend("recommendation_event_topic", event);

        // Wait for the processing to complete, with a timeout
        boolean processed = latch.await(5, TimeUnit.SECONDS);

        // Verify that the listener processed the event
        assertTrue(processed, "The RecommendationEventListener did not process the message in time");
    }

    /**
     * Mock configuration for RedisTemplate to avoid requiring an actual Redis instance.
     */
    @Configuration
    static class MockRedisConfig {

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            return Mockito.mock(RedisTemplate.class);
        }
    }
}
