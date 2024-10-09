package faang.school.analytics.service.impl.analyticsevent;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.FollowerEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private FollowerEventDto followerEvent;
    private AnalyticsEvent analyticsEvent;

    @Test
    void saveFollowerEvent() {
        // given
        followerEvent = buildFollowerEvent();
        analyticsEvent = buildAnalyticsFromFollowerEvent();
        when(analyticsEventMapper.toEntity(followerEvent)).thenReturn(analyticsEvent);
        // when
        analyticsEventService.saveFollowerEvent(followerEvent);
        // then
        verify(analyticsEventMapper).toEntity(followerEvent);
        verify(analyticsEventRepository).save(analyticsEvent);
    }

    private FollowerEventDto buildFollowerEvent() {
        return FollowerEventDto.builder()
                .followerId(1)
                .followeeId(2)
                .build();
    }

    private AnalyticsEvent buildAnalyticsFromFollowerEvent() {
        return AnalyticsEvent.builder()
                .actorId(followerEvent.followerId())
                .receiverId(followerEvent.followeeId())
                .eventType(EventType.FOLLOWER)
                .build();
    }
}