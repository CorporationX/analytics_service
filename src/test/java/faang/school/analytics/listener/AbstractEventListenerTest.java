package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.redis.listener.AbstractEventListener;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message message;
    private TestEventListener testEventListener;

    @BeforeEach
    void setUp() {
        testEventListener = spy(new TestEventListener(objectMapper, new ChannelTopic("test-topic")));

    }

    @Test
    void shouldProcessValidMessage() throws JsonProcessingException {
        String validJson = "{\"message\":\"This is a valid message\"}";
        TestEvent event = new TestEvent("This is a valid message");

        when(message.getBody()).thenReturn(validJson.getBytes());
        when(objectMapper.readValue(validJson, TestEvent.class)).thenReturn(event);

        assertDoesNotThrow(() -> testEventListener.onMessage(message, null));

        verify(objectMapper, times(1)).readValue(validJson, TestEvent.class);
        verify(testEventListener, times(1)).saveEvent(event);
    }

    @Test
    void shouldHandleInvalidJsonGracefully() throws JsonProcessingException {
        String invalidJson = "invalid json";

        when(message.getBody()).thenReturn(invalidJson.getBytes());
        when(objectMapper.readValue(anyString(), eq(TestEvent.class)))
                .thenThrow(new JsonProcessingException("Invalid JSON") {});

        assertDoesNotThrow(() -> testEventListener.onMessage(message, null));

        verify(testEventListener, never()).saveEvent(any(TestEvent.class));
    }

    private static class TestEventListener extends AbstractEventListener<TestEvent> {

        public TestEventListener(ObjectMapper objectMapper, ChannelTopic topic) {
            super(objectMapper, topic);
        }

        @Override
        public void saveEvent(TestEvent event) {
        }

        @Override
        public Class<TestEvent> getEventType() {
            return TestEvent.class;
        }

        @Override
        protected void handleJsonParsingError(String messageBody, JsonProcessingException e) {
        }
    }

    @Getter
    @Setter
    private static class TestEvent {
        private String message;

        public TestEvent(String message) {
            this.message = message;
        }
    }
}
