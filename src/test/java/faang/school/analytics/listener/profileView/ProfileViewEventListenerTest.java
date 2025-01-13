package faang.school.analytics.listener.profileView;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.profileView.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class ProfileViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Logger log;

    @InjectMocks
    private ProfileViewEventListener profileViewEventListener;

    private ProfileViewEvent testEvent;
    private AnalyticsEvent testAnalyticsEvent;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        testEvent = ProfileViewEvent.builder()
                .userId(1L)
                .userIdViewing(2L)
                .dateTimeOfViewing("2025-01-08T20:00:00")
                .build();

        testAnalyticsEvent = new AnalyticsEvent();
        testAnalyticsEvent.setActorId(1L);
        testAnalyticsEvent.setReceiverId(2L);

        // Настраиваем mock для ObjectMapper и AnalyticsEventMapper
        when(objectMapper.readValue(any(byte[].class), eq(ProfileViewEvent.class))).thenReturn(testEvent);
        when(analyticsEventMapper.toEntity(testEvent)).thenReturn(testAnalyticsEvent);
    }

    @Test
    void onMessage_ShouldProcessAndSaveEvent() throws Exception {
        String jsonMessage = "{\"userId\":1,\"userIdViewing\":2,\"dateTimeOfViewing\":\"2025-01-08T20:00:00\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(jsonMessage.getBytes(StandardCharsets.UTF_8));

        profileViewEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(message.getBody(), ProfileViewEvent.class);
        verify(analyticsEventMapper, times(1)).toEntity(testEvent);
        verify(analyticsEventService, times(1)).saveEvent(testAnalyticsEvent);

        ArgumentCaptor<AnalyticsEvent> captor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventService).saveEvent(captor.capture());
        AnalyticsEvent savedEvent = captor.getValue();
        assertEquals(testAnalyticsEvent.getActorId(), savedEvent.getActorId());
        assertEquals(testAnalyticsEvent.getReceiverId(), savedEvent.getReceiverId());
    }
}
