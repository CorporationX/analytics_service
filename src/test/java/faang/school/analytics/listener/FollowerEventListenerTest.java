package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.redis.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {
    @InjectMocks
    private FollowerEventListener followerEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository repository;
    @Mock
    private Message message;
    private FollowerEventDto followerEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2023, 9, 12, 0, 0, 0);
        followerEventDto = FollowerEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .eventType(EventType.FOLLOWER)
                .timestamp(dateTime)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(1L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(dateTime)
                .build();


        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(message.getBody(), FollowerEventDto.class)).thenReturn(followerEventDto);
        when(analyticsEventMapper.toEntity(followerEventDto)).thenReturn(analyticsEvent);
    }

    @Test
    void testSaveMethod() {
        followerEventListener.onMessage(message, null);
        verify(repository).save(analyticsEvent);
    }

    @Test
    void testMapperToEntity() {
        followerEventListener.onMessage(message, null);
        verify(analyticsEventMapper).toEntity(followerEventDto);
    }

    @Test
    void testReadValueMethod() throws IOException {
        followerEventListener.onMessage(message, null);
        verify(objectMapper).readValue(message.getBody(), FollowerEventDto.class);
    }
}