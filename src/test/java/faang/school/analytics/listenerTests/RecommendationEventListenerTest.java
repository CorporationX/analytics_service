package faang.school.analytics.listenerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.listener.RecommendationEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private RecommendationEventListener listener;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        listener = new RecommendationEventListener(analyticsEventService, objectMapper);
    }

    @Test
    void testOnMessage() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        RecommendationEvent event = new RecommendationEvent(1L, 2L, 3L, LocalDateTime.now());
        String eventJson = objectMapper.writeValueAsString(event);


        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(eventJson.getBytes());


        listener.onMessage(message, null);


        verify(analyticsEventService, times(1)).processRecommendationEvent(event);
    }
}

