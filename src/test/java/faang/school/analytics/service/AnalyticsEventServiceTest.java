package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @BeforeEach
    public void setUp() {
        analyticsEventService = new AnalyticsEventServiceImpl(analyticsEventRepository,
                analyticsEventMapper);
    }

    @Test
    @DisplayName("Save fail due to existing entity")
    public void saveEventFailTest() {
        AnalyticsEvent testEvent = getEventBuild(1, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 8,
                        8, 0, 0));
        when(analyticsEventRepository.findById(1L)).thenReturn(Optional.ofNullable(testEvent));

        assertThrows(IllegalArgumentException.class,
                () -> analyticsEventService.saveEvent(testEvent));
    }

    @Test
    @DisplayName("Successfully saving an event")
    public void saveEventTest() {
        AnalyticsEvent testEvent = getEventBuild(1, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 8,
                        8, 0, 0));
        when(analyticsEventRepository.findById(1L)).thenReturn(Optional.empty());

        analyticsEventService.saveEvent(testEvent);

        verify(analyticsEventRepository, times(1)).save(testEvent);
    }

    @Test
    @DisplayName("Get Analytics by interval")
    public void getAnalyticsByIntervalTest() {
        long receiverId = 2;
        EventType eventType = EventType.FOLLOWER;
        Interval interval = Interval.MONTH;
        LocalDateTime from = LocalDateTime.of(2025, 2,
                15, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 3,
                15, 0, 0);
        AnalyticsEvent firstEvent = getEventBuild(1, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 8,
                        5, 0, 0));
        AnalyticsEvent secondEvent = getEventBuild(2, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 2,
                        25, 0, 0));
        AnalyticsEvent thirdEvent = getEventBuild(3, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 7,
                        25, 0, 0));
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(firstEvent, secondEvent, thirdEvent));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId,
                eventType, interval, from, to);

        assertEquals(2, result.size());
        assertEquals(3, result.get(0).id());
        assertEquals(1, result.get(1).id());
    }

    @Test
    @DisplayName("Get Analytics by to-from range")
    public void getAnalyticsByDateRangeTest() {
        long receiverId = 2;
        EventType eventType = EventType.FOLLOWER;
        Interval interval = null;
        LocalDateTime from = LocalDateTime.of(2025, 2,
                15, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 3,
                15, 0, 0);
        AnalyticsEvent firstEvent = getEventBuild(1, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 8,
                        5, 0, 0));
        AnalyticsEvent secondEvent = getEventBuild(2, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 2,
                        25, 0, 0));
        AnalyticsEvent thirdEvent = getEventBuild(3, 2, 3,
                EventType.FOLLOWER, LocalDateTime.of(2025, 7,
                        25, 0, 0));
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(firstEvent, secondEvent, thirdEvent));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId,
                eventType, interval, from, to);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).id());
    }

    private AnalyticsEvent getEventBuild(int id, long receiverId,
                                         long actorId, EventType type, LocalDateTime dateReceived) {
        return AnalyticsEvent.builder()
                .id(id)
                .receiverId(receiverId)
                .actorId(actorId)
                .eventType(type)
                .receivedAt(dateReceived)
                .build();
    }
}
