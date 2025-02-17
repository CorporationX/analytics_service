package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {

    @InjectMocks
    AnalyticsEventServiceImpl analyticsEventServiceImpl;

    @Mock
    private AnalyticsEventRepository analyticsEventRepositoryMock;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapperMock;

    private AnalyticsEvent analyticsEventWithNullReceivedAt;
    private AnalyticsEvent analyticsEventWithNullEventType;
    private AnalyticsEvent analyticsEvent;


    @BeforeEach
    void setUp() {
        this.analyticsEventWithNullReceivedAt = createRandomEvent();
        this.analyticsEventWithNullReceivedAt.setReceivedAt(null);

        this.analyticsEventWithNullEventType = createRandomEvent();
        this.analyticsEventWithNullEventType.setEventType(null);

        this.analyticsEvent = createRandomEvent();
    }

    private AnalyticsEvent createRandomEvent() {
        EventType randomEventType = getRandomEventType();
        LocalDateTime randomDateTime =
                getRandomDateTime(Instant.now().minus(Duration.ofDays(365)), Instant.now().minus(Duration.ofDays(1)));

        return AnalyticsEvent.builder()
                .id(getRandomLong())
                .receiverId(getRandomLong())
                .actorId(getRandomLong())
                .eventType(randomEventType)
                .receivedAt(randomDateTime)
                .build();
    }

    private EventType getRandomEventType() {
        return EventType.of(ThreadLocalRandom.current().nextInt(EventType.values().length));
    }

    private LocalDateTime getRandomDateTime(Instant start, Instant end) {
        long randomSeconds = ThreadLocalRandom.current()
                .nextLong(start.getEpochSecond(), end.getEpochSecond());
        Instant instant = Instant.ofEpochSecond(randomSeconds);
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    private long getRandomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    @Test
    void saveEventIfEventTypeIsNullThenThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> analyticsEventServiceImpl.saveEvent(analyticsEventWithNullEventType)
        );
    }

    @Test
    void saveEventIfReceivedAtIsNullThenThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> analyticsEventServiceImpl.saveEvent(analyticsEventWithNullReceivedAt)
        );
    }

    @Test
    void saveEvent() {
        analyticsEventServiceImpl.saveEvent(analyticsEvent);
        Mockito.verify(analyticsEventRepositoryMock, Mockito.times(1))
                .save(analyticsEvent);
    }

    @Test
    void getAnalyticsIfEventTypeIsNullShouldThrowIllegalArgumentException() {
        long receiverId = getRandomLong();
        EventType eventType = null;
        Interval interval = Interval.YEAR;
        LocalDateTime from = getRandomDateTime(Instant.now().minus(Duration.ofDays(365)), Instant.now());
        LocalDateTime to = getRandomDateTime(Instant.now().minus(Duration.ofDays(365)), Instant.now());

        assertThrows(
                IllegalArgumentException.class,
                () -> analyticsEventServiceImpl.getAnalytics(receiverId, eventType, interval, from, to)
        );
    }

    @Test
    void getAnalyticsIfIntervalAndFromAndToIsNullShouldThrowIllegalArgumentException() {
        long receiverId = getRandomLong();
        EventType eventType = getRandomEventType();
        Interval interval = null;
        LocalDateTime from = null;
        LocalDateTime to = null;

        assertThrows(
                IllegalArgumentException.class,
                () -> analyticsEventServiceImpl.getAnalytics(receiverId, eventType, interval, from, to)
        );
    }

    @Test
    void getAnalyticsWithInterval() {
        long receiverId = getRandomLong();
        EventType eventType = getRandomEventType();
        Interval interval = Interval.YEAR;
        LocalDateTime from = null;
        LocalDateTime to = null;

        Mockito.when(analyticsEventRepositoryMock.findByReceiverIdAndEventTypeAndReceivedAtBetweenOrderByReceivedAtDesc(
                        anyLong(), any(EventType.class), any(LocalDateTime.class), any(LocalDateTime.class))
                )
                .thenReturn(Stream.of(analyticsEvent));

        List<AnalyticsEventDto> result =
                analyticsEventServiceImpl.getAnalytics(receiverId, eventType, interval, from, to);

        Mockito.verify(analyticsEventRepositoryMock, Mockito.times(1))
                .findByReceiverIdAndEventTypeAndReceivedAtBetweenOrderByReceivedAtDesc(
                        anyLong(), any(EventType.class), any(LocalDateTime.class), any(LocalDateTime.class));

        Assertions.assertEquals(analyticsEventMapperMock.toAnalyticsEventDto(analyticsEvent), result.get(0));
    }

    @Test
    void getAnalyticsWithFromAndTo() {
        long receiverId = getRandomLong();
        EventType eventType = getRandomEventType();
        Interval interval = null;
        LocalDateTime from = getRandomDateTime(Instant.now().minus(Duration.ofDays(365)), Instant.now());
        LocalDateTime to = from.plusDays(1);

        Mockito.when(analyticsEventRepositoryMock.findByReceiverIdAndEventTypeAndReceivedAtBetweenOrderByReceivedAtDesc(
                        anyLong(), any(EventType.class), any(LocalDateTime.class), any(LocalDateTime.class))
                )
                .thenReturn(Stream.of(analyticsEvent));

        List<AnalyticsEventDto> result =
                analyticsEventServiceImpl.getAnalytics(receiverId, eventType, interval, from, to);

        Mockito.verify(analyticsEventRepositoryMock, Mockito.times(1))
                .findByReceiverIdAndEventTypeAndReceivedAtBetweenOrderByReceivedAtDesc(
                        anyLong(), any(EventType.class), any(LocalDateTime.class), any(LocalDateTime.class));

        Assertions.assertEquals(analyticsEventMapperMock.toAnalyticsEventDto(analyticsEvent), result.get(0));
    }

}
