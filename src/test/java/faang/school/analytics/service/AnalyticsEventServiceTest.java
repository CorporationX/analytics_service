package faang.school.analytics.service;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void testSaveFollowerEvent() {

        FollowerEvent followerEvent = FollowerEvent.builder()
                .followerId(1L)
                .followeeId(2L)
                .projectId(null)
                .eventTime(LocalDateTime.now())
                .build();

        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .receiverId(followerEvent.followeeId())
                .actorId(followerEvent.followerId())
                .eventType(EventType.FOLLOWER)
                .receivedAt(followerEvent.eventTime())
                .build();

        when(eventMapper.toAnalyticsEvent(followerEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveFollowerEvent(followerEvent);

        verify(eventMapper, times(1)).toAnalyticsEvent(followerEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }
}
