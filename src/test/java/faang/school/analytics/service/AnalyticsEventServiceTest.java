package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;
    @Spy
    private AnalyticsEventMapper analyticsEventMapper = new AnalyticsEventMapperImpl();
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent firstEvent;
    private AnalyticsEvent secondEvent;

    Iterable<AnalyticsEvent> iterable;

    @BeforeEach
    void setUp() {

        LocalDateTime currentTime = LocalDateTime.now();
        firstEvent = AnalyticsEvent.builder().
                id(1)
                .receiverId(2)
                .actorId(3)
                .eventType(EventType.FOLLOWER)
                .receivedAt(currentTime)
                .build();
        secondEvent = AnalyticsEvent.builder().
                id(2)
                .receiverId(5)
                .actorId(6)
                .eventType(EventType.FOLLOWER)
                .receivedAt(currentTime.minusMonths(2))
                .build();
        iterable = List.of(firstEvent, secondEvent);
    }

    @Test
    void saveEventTest() {
        analyticsEventService.saveEvent(firstEvent);

        verify(repository).save(firstEvent);
    }

    @Test
    void getAnalyticsTest() {
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();

        when(repository.findByReceiverIdAndEventType(1, EventType.FOLLOWER)).thenReturn(iterable);

        List<AnalyticsEventDto> expected = List.of(analyticsEventMapper.toDto(firstEvent));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, 2, startDate, endDate);

        assertEquals(1, result.size());
        assertEquals(expected, result);
    }

    @Test
    void getAnalyticsInvokesMethodsTest() {
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();

        when(repository.findByReceiverIdAndEventType(1, EventType.FOLLOWER)).thenReturn(iterable);

        analyticsEventService.getAnalytics(1, 2, startDate, endDate);

        verify(repository).findByReceiverIdAndEventType(1,EventType.FOLLOWER);
    }
}