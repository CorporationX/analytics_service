package faang.school.analytics.service.event;

import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    @Captor
    private ArgumentCaptor<AnalyticsEvent> captor;
    private AnalyticsEvent event;
    private FollowerEventDto eventDto;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.now())
                .build();
        eventDto = FollowerEventDto.builder()
                .followeeId(1L)
                .followerId(2L)
                .subscriptionTime(LocalDateTime.now())
                .build();
    }

    @Test
    void testWhenSaveAnalyticsEventThenEntityIsMappedAndSaved() {
        when(analyticsEventMapper.toEntity(any(FollowerEventDto.class))).thenReturn(event);
        analyticsEventService.saveAnalyticsEvent(eventDto);

        verify(analyticsEventMapper).toEntity(eventDto);
        verify(analyticsEventRepository).save(captor.capture());
    }
}