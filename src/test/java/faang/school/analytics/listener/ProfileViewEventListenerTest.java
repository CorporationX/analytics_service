package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {

    @InjectMocks
    private ProfileViewEventListener profileViewEventListener;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    private String currentEvent;
    private ProfileViewEventDto expectedEventDto;

    @BeforeEach
    public void setUp() {
        expectedEventDto = new ProfileViewEventDto(1L, 2L, LocalDateTime.parse("2024-10-11T15:34:24"));
        currentEvent = "{\"receiverId\":1,\"actorId\":2,\"receivedAt\":\"2024-10-11T15:34:24\"}";
    }

    @Test
    public void handleMessage_WhenSuccess() throws JsonProcessingException {
        when(objectMapper.readValue(currentEvent, ProfileViewEventDto.class)).thenReturn(expectedEventDto);
        profileViewEventListener.handleMessage(currentEvent);
        verify(analyticsEventService).saveEvent(expectedEventDto);
    }

    @Test
    public void handleMessage_WhenFailure_ExpectedException() throws JsonProcessingException {
        when(objectMapper.readValue(currentEvent, ProfileViewEventDto.class)).thenThrow(new JsonProcessingException("Error processing JSON") {});
        assertThrows(RuntimeException.class, () -> profileViewEventListener.handleMessage(currentEvent));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}
