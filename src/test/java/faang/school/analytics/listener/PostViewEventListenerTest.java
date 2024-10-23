package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevent.AnalyticsEventMapperImpl;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class PostViewEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private PostViewEventListener postEventListener;

    private PostViewEvent postEvent;

    @BeforeEach
    void setUp() {
        postEvent = PostViewEvent.builder().build();
    }

    @Test
    void testOnMessageOk() throws IOException {
        byte[] messageBody = "{\"postId\":123,\"authorId\":456,\"userId\":789,\"receivedAt\":\"2024-08-16T12:00:00\\\"}".getBytes();
        doReturn(messageBody).when(message).getBody();
        doReturn(postEvent).when(objectMapper).readValue(messageBody, PostViewEvent.class);

        postEventListener.onMessage(message, null);

        verify(objectMapper).readValue(messageBody, PostViewEvent.class);
        verify(analyticsEventService).saveEvent(any(AnalyticsEvent.class));
        verify(analyticsEventMapper).toEntity(postEvent);
    }

    @Test
    @DisplayName("On Message Test Throws Exception")
    void onMessageShouldThrowException() throws IOException {
        byte[] messageBody = "{\"invalid\":\"json\"}".getBytes();
        doReturn(messageBody).when(message).getBody();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> postEventListener.onMessage(message, null));

        verify(objectMapper).readValue(messageBody, PostViewEvent.class);
        verify(analyticsEventService, never()).saveEvent(any(AnalyticsEvent.class));
        verify(analyticsEventMapper, never()).toEntity(postEvent);
        verifyNoMoreInteractions(objectMapper, analyticsEventService);
    }
}