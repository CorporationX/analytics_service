package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.Topic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TestEventListener eventListener;

    @Test
    @DisplayName("Successful message processing")
    void testOnMessageSuccessfullyProcessesEvent() throws Exception {
        String jsonMessage = "{\"id\": 1, \"message\": \"Test Event\"}";
        TestEvent testEvent = new TestEvent(1, "Test Event");

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(jsonMessage, TestEvent.class)).thenReturn(testEvent);

        TestEventListener spyEventListener = spy(eventListener);
        spyEventListener.onMessage(message, null);

        ArgumentCaptor<TestEvent> eventCaptor = ArgumentCaptor.forClass(TestEvent.class);
        verify(spyEventListener, times(1)).saveEvent(eventCaptor.capture());
        assertEquals(testEvent, eventCaptor.getValue(), "Сохраненное событие должно совпадать с ожидаемым");
    }

    @Test
    @DisplayName("Message processing throws exception when JSON is invalid")
    void testOnMessageThrowsExceptionForInvalidJson() throws Exception {
        String invalidJsonMessage = "invalid json";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(invalidJsonMessage.getBytes());
        when(objectMapper.readValue(invalidJsonMessage, TestEvent.class)).thenThrow(JsonProcessingException.class);

        TestEventListener eventListenerSpy = spy(eventListener);

        assertDoesNotThrow(() -> eventListenerSpy.onMessage(message, null));
        verify(eventListenerSpy, never()).saveEvent(any());
    }

    static class TestEventListener extends AbstractEventListener<TestEvent> {

        public TestEventListener(ObjectMapper objectMapper, Topic topic) {
            super(objectMapper, topic);
        }

        @Override
        public void saveEvent(TestEvent event) {
        }

        @Override
        public Class<TestEvent> getEventType() {
            return TestEvent.class;
        }
    }

    @Data
    static class TestEvent {
        private int id;
        private String message;

        public TestEvent(int id, String message) {
            this.id = id;
            this.message = message;
        }
    }
}