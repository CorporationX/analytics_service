package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @InjectMocks
    private ProfileViewEventListener profileViewEventListener;

    private Message message;
    private ProfileViewEvent profileViewEvent;

    @BeforeEach
    public void setUp() {
        String event = "{\"userId\":1,\"guestId\":2,\"viewDateTime\":\"2024-10-17T12:34:56\"}";
        message = new DefaultMessage("profile_view_channel".getBytes(), event.getBytes());
        profileViewEvent = new ProfileViewEvent();
        profileViewEvent.setReceiverId(1L);
        profileViewEvent.setActorId(2L);
        profileViewEvent.setTimestamp(LocalDateTime.of(2024, 10, 17, 12, 34, 56));
    }

    @Test
    public void testHandleEventFail() {
        message = new DefaultMessage("testChannel".getBytes(), "Test".getBytes());
        try {
            when(objectMapper.readValue(message.getBody(), ProfileViewEvent.class))
                    .thenThrow(RuntimeException.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> profileViewEventListener.handleEvent(message, ProfileViewEvent.class));
    }

    @Test
    public void testHandleEventSuccess() {
        try {
            when(objectMapper.readValue(message.getBody(), ProfileViewEvent.class))
                    .thenReturn(profileViewEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ProfileViewEvent testProfileViewEvent = profileViewEventListener.handleEvent(message, ProfileViewEvent.class);
        Assertions.assertNotNull(testProfileViewEvent);
        Assertions.assertEquals(profileViewEvent.getReceiverId(), testProfileViewEvent.getReceiverId());
        Assertions.assertEquals(profileViewEvent.getActorId(), testProfileViewEvent.getActorId());
        Assertions.assertEquals(profileViewEvent.getTimestamp(), testProfileViewEvent.getTimestamp());
    }

    @Test
    public void testSendAnalyticsSuccess() {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(profileViewEvent);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        profileViewEventListener.sendAnalytics(profileViewEvent);

        verify(analyticsEventService).saveEvent(analyticsEvent);
    }
}
