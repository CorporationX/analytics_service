package faang.school.analytics.service;

import faang.school.analytics.dto.Interval;
import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.request.GetAnalyticsRequestDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.repository.criteria.AnalyticsGetCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = new AnalyticsEventMapperImpl();

    @Captor
    ArgumentCaptor<AnalyticsGetCriteria> analyticsGetCriteriaCaptor;

    private final Long eventReceiverId = 2L;
    private final Long eventActorId = 1L;
    private final EventType eventType = EventType.RECOMMENDATION_RECEIVED;
    private final LocalDateTime eventCreatedAt = LocalDateTime.now().minusHours(1);

    private final AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto(
            null,
            eventReceiverId,
            eventActorId,
            eventType,
            eventCreatedAt
    );

    private final GetAnalyticsRequestDto getAnalyticsRequestDtoLastWeek = new GetAnalyticsRequestDto(
            eventReceiverId,
            eventType,
            Interval.LAST_WEEK,
            null,
            null
    );

    @Test
    void saveEventMapsToEntityAndSaves() {
        AnalyticsEventDto result = analyticsEventService.saveEvent(analyticsEventDto);

        verify(analyticsEventMapper).toAnalyticsEvent(analyticsEventDto);
        verify(analyticsEventRepository).save(Mockito.any());
        assertEquals(eventActorId, result.actorId());
        assertEquals(eventReceiverId, result.receiverId());
        assertEquals(eventType, result.eventType());
    }

    @Test
    void getAnalyticConvertsToCriteriaAndCallsRepo() {
        analyticsEventService.getAnalytics(getAnalyticsRequestDtoLastWeek);

        verify(analyticsEventRepository).findByCriteria(analyticsGetCriteriaCaptor.capture());
        AnalyticsGetCriteria criteria = analyticsGetCriteriaCaptor.getValue();
        assertEquals(getAnalyticsRequestDtoLastWeek.receiverId(), criteria.getReceiverId());
        assertEquals(getAnalyticsRequestDtoLastWeek.eventType(), criteria.getEventType());
        assertEquals(getAnalyticsRequestDtoLastWeek.interval(), criteria.getInterval());
    }
}