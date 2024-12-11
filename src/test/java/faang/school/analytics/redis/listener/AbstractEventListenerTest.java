package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private EventListenerForTest eventListenerForTest;

    @Mock
    private Message message;

    @Test
    public void mapMessageTest() throws IOException {
        EventForTest eventForTest = new EventForTest();
        when(objectMapper.readValue(message.getBody(), EventForTest.class)).thenReturn(eventForTest);

        eventListenerForTest.mapMessage(message, EventForTest.class);

        verify(objectMapper,times(1)).readValue(message.getBody(), EventForTest.class);
    }

    @Test
    public void mapMessageThrowsExceptionTest() throws IOException {
        when(objectMapper.readValue(message.getBody(), EventForTest.class)).thenThrow(IOException.class);

        assertThrows(IllegalArgumentException.class, () ->
                eventListenerForTest.mapMessage(message, EventForTest.class));
    }

    @Test
    public void saveTest() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        eventListenerForTest.save(analyticsEvent);

        verify(analyticsEventService,times(1)).saveEvent(analyticsEvent);
    }
}