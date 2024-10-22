package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {
    @InjectMocks
    LikeEventListener likeEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    private LikeEvent event;
    private String jsonEvent;

    @BeforeEach
    void setUp() {
        event = new LikeEvent();
        jsonEvent = "json";
    }

    @Test
    void handleMessageTest() throws JsonProcessingException {
        when(objectMapper.readValue(jsonEvent, LikeEvent.class)).thenReturn(event);

        likeEventListener.handleMessage(jsonEvent);

        verify(analyticsEventService, times(1)).saveLikeEvent(event);
    }
}
