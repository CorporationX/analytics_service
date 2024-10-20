package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.Topic;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerListTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Topic topic;

    @InjectMocks
    private TestEventListener eventListener;

    @Test
    @DisplayName("Successful message processing")
    void testOnMessageSuccessful() throws IOException {
        String jsonMessage = "{\"id\": 1, \"message\": \"Test Event\"}";
        TestEvent testEvent1 = new TestEvent(1, "Test Event 1");
        TestEvent testEvent2 = new TestEvent(2, "Test Event 2");
        List<TestEvent> testEventList = List.of(testEvent1, testEvent2);

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(jsonMessage.getBytes());

        TypeFactory typeFactory = TypeFactory.defaultInstance();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, TestEvent.class);

        when(objectMapper.getTypeFactory()).thenReturn(typeFactory);
        when(objectMapper.readValue(eq(jsonMessage.getBytes()), eq(collectionType))).thenReturn(testEventList);

        TestEventListener spyEventListener = spy(eventListener);
        spyEventListener.onMessage(message, null);

        verify(spyEventListener, times(testEventList.size())).saveEvent(any(TestEvent.class));
    }

    @Test
    @DisplayName("Throw exception when message processing")
    void testOnMessageException() throws IOException {
        String jsonMessage = "{\"id\": 1, \"message\": \"Test Event\"}";
        TestEvent testEvent = new TestEvent(1, "Test Event");
        List<TestEvent> testEventList = List.of(testEvent);

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(jsonMessage.getBytes());

        TypeFactory typeFactory = TypeFactory.defaultInstance();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, TestEvent.class);

        when(objectMapper.getTypeFactory()).thenReturn(typeFactory);
        when(objectMapper.readValue(eq(jsonMessage.getBytes()), eq(collectionType))).thenReturn(testEventList);

        TestEventListener spyEventListener = spy(eventListener);
        doThrow(new RuntimeException()).when(spyEventListener).saveEvent(testEvent);

        assertThatThrownBy(() -> spyEventListener.onMessage(message, null))
                .isInstanceOf(RuntimeException.class);
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

    static class TestEventListener extends AbstractEventListenerList<TestEvent> {
        public TestEventListener(ObjectMapper objectMapper, Topic topic) {
            super(objectMapper, topic);
        }

        @Override
        protected String getEventTypeName() {
            return "Test events";
        }

        @Override
        public void saveEvent(TestEvent event) {
        }

        @Override
        public Class<TestEvent> getEventType() {
            return TestEvent.class;
        }
    }
}