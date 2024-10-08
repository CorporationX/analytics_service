package faang.school.analytics.config.redis.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisProfileViewEventSubscriberTest {
    private static final String JSON = "{\"receiverId\":1,\"actorId\":2,\"receivedAt\":\"2024-10-08T18:46:50\"}";
    private static final long RECEIVER_ID = 1L;
    private static final long ACTOR_ID = 2L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1);

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private RedisProfileViewEventSubscriber redisProfileViewEventSubscriber;

    @Test
    @DisplayName("Received message of redis topic successful")
    void testOnMessage() throws IOException {

        Message message = mock(Message.class);
        ProfileViewEventDto profileViewEventDto = new ProfileViewEventDto(RECEIVER_ID, ACTOR_ID, LOCAL_DATE_TIME);
        byte[] bytes = JSON.getBytes(StandardCharsets.UTF_8);
        when(message.getBody()).thenReturn(bytes);
        when(objectMapper.readValue(bytes, ProfileViewEventDto.class)).thenReturn(profileViewEventDto);

        redisProfileViewEventSubscriber.onMessage(message, "test".getBytes());
        verify(analyticsEventService).saveEvent(any(AnalyticsEvent.class));
    }
}