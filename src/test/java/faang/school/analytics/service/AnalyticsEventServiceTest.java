package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    @Mock
    EventMapper eventMapper;
    @InjectMocks
    AnalyticsEventService analyticsEventService;

    AnalyticsEvent analyticsEvent;
    AnalyticsDto analyticsDto;
    AnalyticsFilterDto filterDto;
    FollowEventDto followEventDto;

    @BeforeEach
    void setUp() {
        analyticsEvent = AnalyticsEvent.builder().receiverId(1L).eventType(EventType.FOLLOWER).receivedAt(LocalDateTime.now()).build();
        analyticsDto = AnalyticsDto.builder().receiverId(1L).eventType(EventType.FOLLOWER).receivedAt(LocalDateTime.now()).build();
        filterDto = new AnalyticsFilterDto(1L, EventType.FOLLOWER, null, null);
        followEventDto = new FollowEventDto();

    }

    @Test
    void FollowEventSaveTest() {
        Mockito.when(eventMapper.toEntity(followEventDto)).thenReturn(analyticsEvent);
        analyticsEventService.followEventSave(followEventDto);
        Mockito.verify(analyticsEventRepository, Mockito.times(1)).save(analyticsEvent);
    }

    @Test
    void getAnalyticsTest() {
        Mockito.when(eventMapper.toDto(analyticsEvent)).thenReturn(analyticsDto);
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(Mockito.anyLong(), Mockito.any(EventType.class))).thenReturn(List.of(analyticsEvent));

        var result = analyticsEventService.getAnalytics(filterDto);
        Assertions.assertEquals(result, List.of(analyticsDto));
    }

    @Test
    void getAnalyticsEmptyListTest() {
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(Mockito.anyLong(), Mockito.any(EventType.class))).thenReturn(List.of(analyticsEvent));
        filterDto.setStart(LocalDateTime.MAX);

        var result = analyticsEventService.getAnalytics(filterDto);
        Assertions.assertTrue(result.isEmpty());
    }
}
