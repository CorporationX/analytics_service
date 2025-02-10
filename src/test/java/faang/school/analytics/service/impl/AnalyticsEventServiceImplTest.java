package faang.school.analytics.service.impl;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.Interval;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;


    private static final long ACTOR_ID = 2L;
    private static final long RECEIVER_ID = 3L;
    private static final EventType EVENT_TYPE = EventType.POST_LIKE;


    private AnalyticsEvent analyticsEvent = new AnalyticsEvent();
    private AnalyticsEvent analyticsEventFailed = new AnalyticsEvent();
    private Stream<AnalyticsEvent> analyticsEventStream;
    private Stream<AnalyticsEvent> analyticsEventStreamInterval;

    @BeforeEach
    void setUp() {
        analyticsEvent.setEventType(EVENT_TYPE);
        analyticsEvent.setActorId(ACTOR_ID);
        analyticsEvent.setReceivedAt(LocalDateTime.now());
        analyticsEvent.setReceiverId(RECEIVER_ID);

        analyticsEventFailed.setEventType(EVENT_TYPE);

        analyticsEventStream = prepareStreamForTest(false);
        analyticsEventStreamInterval = prepareStreamForTest(true);

        analyticsEventMapper = new AnalyticsEventMapperImpl();

    }

    private Stream<AnalyticsEvent> prepareStreamForTest(boolean interval) {
        List<AnalyticsEvent> analyticsEventList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalDateTime date = !interval ? LocalDateTime.now() : LocalDateTime.now().minusMonths(5);
            analyticsEventList.add(new AnalyticsEvent(i, RECEIVER_ID, ACTOR_ID, EVENT_TYPE, date));
        }
        return analyticsEventList.stream();
    }

    @Test
    public void testSaveEvent() {
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    public void testSaveEventNull() {
        Assert.assertThrows(
                DataValidationException.class,
                () -> analyticsEventService.saveEvent(new AnalyticsEvent()));
    }

    @Test
    public void testSaveEventActorId() {
        Assert.assertThrows(
                DataValidationException.class,
                () -> analyticsEventService.saveEvent(analyticsEventFailed));
    }

    @Test
    public void testGetAnalyticsFromTo() {
        LocalDateTime from = LocalDateTime.now().minusDays(6);
        LocalDateTime to = LocalDateTime.now();
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(analyticsEventStream);
        List<AnalyticsEventDto> list = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, null,
                from, to);
        Assert.assertEquals(10, list.size());
    }

    @Test
    public void testGetAnalyticsFromToNoData() {
        LocalDateTime from = LocalDateTime.now().minusDays(6);
        LocalDateTime to = LocalDateTime.now().minusDays(3);
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(analyticsEventStream);
        List<AnalyticsEventDto> list = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, null,
                from, to);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testGetAnalyticsIntervalFailed() {
        Interval interval = Interval.ONE_MONTH;
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(analyticsEventStreamInterval);
        List<AnalyticsEventDto> list = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, interval,
                null, null);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testGetAnalyticsInterval() {
        Interval interval = Interval.ONE_YEAR;
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(analyticsEventStreamInterval);
        List<AnalyticsEventDto> list = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, interval,
                null, null);
        Assert.assertEquals(10, list.size());
    }
}
