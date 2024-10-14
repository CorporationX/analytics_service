package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticevent.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.ProfileViewEvent;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileViewEventListenerTest {
    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private ProfileViewEventListener profileViewEventListener;

    private ProfileViewEvent profileViewEvent;

    @BeforeEach
    void setUp() {
        profileViewEvent = ProfileViewEvent.builder().build();
    }

    @Test
    void onMessageSuccessfully() throws IOException {
        //given
        String messageEvent = "hi";
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        Mockito.when(message.getBody()).thenReturn(messageEvent.getBytes());
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class),
                        Mockito.eq(ProfileViewEvent.class)))
                .thenReturn(profileViewEvent);
        Mockito.when(analyticsEventMapper.toEntity(profileViewEvent))
                .thenReturn(analyticsEvent);

        //when
        profileViewEventListener.onMessage(message, new byte[0]);

        //Then
        Mockito.verify(message, Mockito.times(1)).getBody();
        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(ProfileViewEvent.class));
        Mockito.verify(analyticsEventService, Mockito.times(1))
                .saveEvent(analyticsEvent);
        Mockito.verify(analyticsEventService, Mockito.times(1))
                .saveEvent(Mockito.argThat(event ->
                        event.getEventType() == EventType.PROFILE_VIEW));
    }

    @Test
    void onMessageDeserializationFails() throws IOException {
        // given
        when(objectMapper.readValue(any(byte[].class), eq(ProfileViewEvent.class)))
                .thenThrow(new JsonProcessingException("Test exception") {
                });
        // when & then
        assertThrows(RuntimeException.class, () -> profileViewEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}