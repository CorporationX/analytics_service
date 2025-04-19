package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.parser.AnalyticsRequestParser;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTests {
    @Mock
    private AnalyticsEventRepository eventRepository;

    @Mock
    private AnalyticsRequestParser parser;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private final long receiverId = 123L;
    private final EventType eventType = EventType.PROJECT_VIEW;
    private final String eventTypeRaw = "PROJECT_VIEW";
    private final String startRaw = "2025-01-01T00:00:00";
    private final String endRaw = "2025-12-31T23:59:59";

    private final LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
    private final LocalDateTime end = LocalDateTime.of(2025, 12, 31, 23, 59);

    private AnalyticsEvent within;
    private AnalyticsEvent before;
    private AnalyticsEvent after;

    @BeforeEach
    void setUp() {
        within = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.of(2025, 4, 18, 16, 0))
                .actorId(1L)
                .build();

        before = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(start.minusDays(5))
                .actorId(2L)
                .build();

        after = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(end.plusDays(5))
                .actorId(3L)
                .build();

        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseDate(startRaw)).thenReturn(start);
        when(parser.parseDate(endRaw)).thenReturn(end);
    }

    @Test
    public void testGetAnalytics_returnsOnlyEventsWithinRange() {
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(within, before, after));

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventTypeRaw, null, startRaw,
                endRaw);

        assertThat(result).containsExactly(within);
    }

    @Test
    public void testGetAnalytics_returnsEmptyListWhenNoEventsInRange() {
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(Stream.of(before, after));

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventTypeRaw, null, startRaw,
                endRaw);

        assertThat(result).isEmpty();
    }

    @Test
    public void testGetAnalytics_returnsMultipleEventsWithinRange() {
        AnalyticsEvent another = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.of(2025, 3, 15, 10, 0))
                .actorId(4L)
                .build();

        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(within, another, before, after));

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventTypeRaw, null, startRaw,
                endRaw);

        assertThat(result).containsExactlyInAnyOrder(within, another);
    }

    @Test
    public void testGetAnalytics_returnsEmptyListWhenRepositoryIsEmpty() {
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(Stream.empty());

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventTypeRaw, null, startRaw,
                endRaw);

        assertThat(result).isEmpty();
    }
}
