package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticRequestDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticServiceTest {

    @InjectMocks
    private AnalyticService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Test
    public void testGetAnalytics() {
        long receiverId = 1;
        EventType type = EventType.POST_LIKE;
        Interval interval = Interval.WEEK;
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<AnalyticsEvent> fakeEvents = createFakeEvents();
        Stream<AnalyticsEvent> fakeEventStream = fakeEvents.stream();
        when(analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(
                receiverId, type, start, end))
                .thenReturn(fakeEventStream);

        AnalyticRequestDto analyticRequestDto = AnalyticRequestDto.builder().receiverId(receiverId).eventType(type).interval(interval).startDate(start).endDate(end).build();
        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(analyticRequestDto);
        assertNotNull(result);
        assertEquals(fakeEvents.size(), result.size());
    }

    private List<AnalyticsEvent> createFakeEvents() {
        List<AnalyticsEvent> fakeEvents = new ArrayList<>();
        fakeEvents.add(
                AnalyticsEvent.builder()
                        .receiverId(1)
                        .actorId(101)
                        .eventType(EventType.POST_LIKE)
                        .receivedAt(LocalDateTime.now().minusDays(1))
                        .build()
        );
        fakeEvents.add(
                AnalyticsEvent.builder()
                        .receiverId(1)
                        .actorId(102)
                        .eventType(EventType.POST_LIKE)
                        .receivedAt(LocalDateTime.now().minusDays(2))
                        .build()
        );
        return fakeEvents;
    }
}