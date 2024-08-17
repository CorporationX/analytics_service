package faang.school.analytics.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.event.DataTransformationException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private Consumer<Object> eventConsumer;

    private TestEventListener sut;

    @BeforeEach
    void setUp() {
        sut = new TestEventListener(objectMapper, analyticsEventService);
    }

    @Test
    void testHandleEvent_Success() throws IOException {
        Message message = mock(Message.class);
        byte[] messageBody = "test message".getBytes();
        when(message.getBody()).thenReturn(messageBody);

        Object event = new Object();
        when(objectMapper.readValue(messageBody, Object.class)).thenReturn(event);

        sut.handleEvent(message, Object.class, eventConsumer);

        verify(eventConsumer).accept(event);
    }

    @Test
    void testHandleEvent_IOException() throws IOException {
        Message message = mock(Message.class);
        byte[] messageBody = "test message".getBytes();
        when(message.getBody()).thenReturn(messageBody);

        when(objectMapper.readValue(messageBody, Object.class)).thenThrow(new IOException());

        assertThrows(DataTransformationException.class, () -> sut.handleEvent(message, Object.class, eventConsumer));
    }

    @Test
    void testPersistAnalyticsData_Success() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        sut.persistAnalyticsData(analyticsEvent);

        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    void testPersistAnalyticsData_Exception() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        doThrow(new RuntimeException()).when(analyticsEventService).saveEvent(analyticsEvent);

        assertThrows(PersistenceException.class, () -> sut.persistAnalyticsData(analyticsEvent));
    }

}