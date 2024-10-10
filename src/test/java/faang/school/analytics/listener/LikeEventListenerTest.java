package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.dto.LikeEventDto;
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
class LikeEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private LikeEventListener likeEventListener;
    private LikeEventDto likeEvent;

    @BeforeEach
    void setUp() {
        likeEvent = LikeEventDto.builder().build();
    }

    @Test
    @DisplayName("On Message Test - Success")
    void testOnMessage() throws IOException {
        byte[] messageBody = "{\"postId\":123,\"authorId\":456,\"userId\":789,\"receivedAt\":\"2024-08-16T12:00:00\\\"}".getBytes();
        doReturn(messageBody).when(message).getBody();
        doReturn(likeEvent).when(objectMapper).readValue(messageBody, LikeEventDto.class);

        likeEventListener.onMessage(message, null);

        verify(objectMapper).readValue(messageBody, LikeEventDto.class);
        verify(analyticsEventService).saveEvent(any(AnalyticsEvent.class));
        verify(analyticsEventMapper).toEntity(likeEvent);
    }

    @Test
    @DisplayName("On Message Test Throws Exception")
    void onMessageShouldThrowException() throws IOException {
        byte[] messageBody = "{\"invalid\":\"json\"}".getBytes();
        doReturn(messageBody).when(message).getBody();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> likeEventListener.onMessage(message, null));

        verify(objectMapper).readValue(messageBody, LikeEventDto.class);
        verify(analyticsEventService, never()).saveEvent(any(AnalyticsEvent.class));
        verify(analyticsEventMapper, never()).toEntity(likeEvent);
        verifyNoMoreInteractions(objectMapper, analyticsEventService);
    }
}