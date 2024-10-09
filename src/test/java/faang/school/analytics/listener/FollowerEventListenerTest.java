package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.dto.FollowerEventDto;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    private Message message;
    private FollowerEventDto followerEventDto;

    @BeforeEach
    void setUp() {
        followerEventDto = buildFollowerEvent();
        message = mock(Message.class);
    }

    @Test
    void onMessage_shouldHandleFollowerEvent() throws Exception {
        // given
        String json = "{\"followerId\": 1, \"followedId\": 2}";
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(any(byte[].class), eq(FollowerEventDto.class)))
                .thenReturn(followerEventDto);
        // when
        followerEventListener.onMessage(message, null);
        // then
        ArgumentCaptor<FollowerEventDto> captor = ArgumentCaptor.forClass(FollowerEventDto.class);
        verify(analyticsEventService).saveFollowerEvent(captor.capture());
        assertEquals(followerEventDto, captor.getValue());
    }

    @Test
    void onMessage_shouldThrowExceptionOnDeserializationError() throws IOException {
        // given
        String invalidJson = "{invalidJson}";
        when(message.getBody()).thenReturn(invalidJson.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(any(byte[].class), eq(FollowerEventDto.class)))
                .thenThrow(new JsonProcessingException("Test exception") {});
        // when & then
        assertThrows(RuntimeException.class, () -> followerEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).saveFollowerEvent(any());
    }

    private FollowerEventDto buildFollowerEvent() {
        return FollowerEventDto.builder()
                .followerId(1)
                .followeeId(2)
                .build();
    }
}