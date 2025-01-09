package faang.school.analytics.listener.postview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostViewEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private PostViewEventListener postViewEventListener;

    @Test
    void testSaveMethodIsCalled() throws JsonProcessingException {
        byte[] byteArray = new byte[0];
        String json = new String(byteArray);
        Message messageMock = mock(Message.class);
        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();
        when(messageMock.getBody()).thenReturn(byteArray);
        when(objectMapper.readValue(json, AnalyticsEventDto.class)).thenReturn(analyticsEventDto);

        postViewEventListener.onMessage(messageMock, byteArray);

        verify(messageMock).getBody();
        verify(objectMapper).readValue(json, AnalyticsEventDto.class);
        verify(analyticsEventService).savePostView(analyticsEventDto);
    }

    @Test
    void testGetJsonProcessingException() throws JsonProcessingException {
        byte[] byteArray = new byte[0];
        String json = new String(byteArray);
        Message messageMock = mock(Message.class);
        when(messageMock.getBody()).thenReturn(byteArray);
        when(objectMapper.readValue(json, AnalyticsEventDto.class)).thenThrow(JsonProcessingException.class);

        assertThrows(RuntimeException.class, () -> postViewEventListener.onMessage(messageMock, byteArray));
    }
}