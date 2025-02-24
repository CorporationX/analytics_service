package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.time.Month;

@ExtendWith(MockitoExtension.class)
class ProfileViewListenerTest {

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private ProfileViewListener profileViewListener;

    @Captor
    private ArgumentCaptor<AnalyticsEventDTO> argumentCaptor;



    @Test
    @DisplayName("The test must process valid message")
    void testProcessValidMessage() throws Exception {
        String jsonEvent = "{\"profileId\":2,\"viewId\":5,\"timestamp\":\"2025-02-23T12:00:00\"}";
        ProfileViewEvent profileViewEvent = ProfileViewEvent.builder()
                .profileId(2)
                .viewId(5)
                .timestamp(LocalDateTime.of(2025, Month.FEBRUARY, 23, 12, 0, 0))
                .build();

        Mockito.when(message.getBody()).thenReturn(jsonEvent.getBytes());
        Mockito.when(objectMapper.readValue(jsonEvent, ProfileViewEvent.class)).thenReturn(profileViewEvent);

        profileViewListener.onMessage(message, null);

        Mockito.verify(analyticsEventService, Mockito.times(1))
                .saveEvent(argumentCaptor.capture());

        AnalyticsEventDTO resultEvent = argumentCaptor.getValue();

        Assertions.assertEquals(EventType.PROFILE_VIEW, resultEvent.eventType());
        Assertions.assertEquals(2, resultEvent.receiverId());
        Assertions.assertEquals(5, resultEvent.actorId());
        Assertions.assertEquals(LocalDateTime.of(2025, Month.FEBRUARY,
                23, 12, 0, 0), resultEvent.receivedAt());
    }

    @Test
    @DisplayName("The test must return IOException on invalid JSON")
    void testProcessInvalidMessage() throws Exception {
        String invalidJson = "invalid_JSON";

        Mockito.when(message.getBody()).thenReturn(invalidJson.getBytes());
        Mockito.doThrow(new JsonMappingException(null, "Error parsing JSON"))
                .when(objectMapper).readValue(Mockito.anyString(), ArgumentMatchers.eq(ProfileViewEvent.class));

        profileViewListener.onMessage(message, null);

        Mockito.verify(analyticsEventService, Mockito.never()).saveEvent(Mockito.any(AnalyticsEventDTO.class));
    }
}