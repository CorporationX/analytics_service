package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static faang.school.analytics.service.AnalyticsEventServiceConstant.ANALYTICS_EVENT_DTO;
import static faang.school.analytics.service.AnalyticsEventServiceConstant.EVENT;
import static faang.school.analytics.service.AnalyticsEventServiceConstant.EVENTS;
import static faang.school.analytics.service.AnalyticsEventServiceConstant.EVENTS2;


@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Test
    @DisplayName("The test is successful save analytic event")
    void testSaveEvent() {
        Mockito.when(analyticsEventRepository.save(EVENT))
                .thenReturn(EVENT);
        Mockito.when(analyticsEventMapper.toEntity(ANALYTICS_EVENT_DTO)).thenReturn(EVENT);

        analyticsEventService.saveEvent(ANALYTICS_EVENT_DTO);

        Mockito.verify(analyticsEventMapper, Mockito.times(1)).toEntity(ANALYTICS_EVENT_DTO);
        Mockito.verify(analyticsEventRepository, Mockito.times(1)).save(EVENT);
    }

    @Test
    @DisplayName("The test is successful get analytic events")
    void testGetListAnalyticEvents() {
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.PROFILE_VIEW))
                .thenReturn(EVENTS);

        Mockito.when(analyticsEventMapper.toDto(Mockito.any(AnalyticsEvent.class)))
                .thenAnswer(invocation -> {
                    AnalyticsEvent currentEvent = invocation.getArgument(0);
                    return AnalyticsEventDTO.builder()
                            .actorId(currentEvent.getActorId())
                            .receiverId(currentEvent.getReceiverId())
                            .eventType(currentEvent.getEventType())
                            .receivedAt(currentEvent.getReceivedAt())
                            .build();
                });

        List<AnalyticsEventDTO> result = analyticsEventService.getAnalytics(1, EventType.PROFILE_VIEW,
                LocalDateTime.now().minusHours(6), LocalDateTime.now().minusHours(1));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.get(0).receivedAt().isAfter(result.get(1).receivedAt()));

        Mockito.verify(analyticsEventRepository, Mockito.times(1))
                .findByReceiverIdAndEventType(1, EventType.PROFILE_VIEW);
        Mockito.verify(analyticsEventMapper, Mockito.times(2))
                .toDto(Mockito.any(AnalyticsEvent.class));
    }

    @Test
    @DisplayName("The test must return an empty list of events for analytics")
    void testGetEmptyListAnalyticEvent() {
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.PROFILE_VIEW))
                .thenReturn(EVENTS2);

        List<AnalyticsEventDTO> result = analyticsEventService.getAnalytics(1, EventType.PROFILE_VIEW,
                LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(1));

        Assertions.assertEquals(0, result.size());
    }
}