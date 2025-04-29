package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    private final AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();
    private final AnalyticsEvent analyticsEvent = new AnalyticsEvent();
    private final long receiverId = 1;
    private final EventType eventType = EventType.FOLLOWER;
    private final Interval interval = Interval.LAST_DAY;
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime from = now.minusMonths(1);
    private final LocalDateTime to = now.minusDays(1);

    private final Stream<AnalyticsEvent> analyticsFromDB = Stream.of(
            AnalyticsEvent.builder().receivedAt(now.minusHours(1)).build(),
            AnalyticsEvent.builder().receivedAt(now.minusHours(2)).build(),
            AnalyticsEvent.builder().receivedAt(now.minusDays(3)).build(),
            AnalyticsEvent.builder().receivedAt(now.minusDays(4)).build(),
            AnalyticsEvent.builder().receivedAt(now.minusMonths(1).minusDays(1)).build()
    );

    private final List<AnalyticsEventDto> analyticsBetweenInterval = List.of(
            AnalyticsEventDto.builder().receivedAt(now.minusHours(1)).build(),
            AnalyticsEventDto.builder().receivedAt(now.minusHours(2)).build()
    );

    private final List<AnalyticsEventDto> analyticsBetweenFromAndTo = List.of(
            AnalyticsEventDto.builder().receivedAt(now.minusDays(3)).build(),
            AnalyticsEventDto.builder().receivedAt(now.minusDays(4)).build()
    );

    @Test
    void saveEvent_shouldSave() {
        assertDoesNotThrow(() -> analyticsEventService.saveEvent(analyticsEventDto));
        verify(analyticsEventMapper, times(1)).toEntity(analyticsEventDto);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void saveEvent_shouldThrowWhenInputDtoIsNull() {
        when(analyticsEventRepository.save(null)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> analyticsEventService.saveEvent(null));
    }

    @Test
    void getAnalytics_shouldGetWhenHasInterval() {
        when(analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(analyticsFromDB);
        List<AnalyticsEventDto> analyticsEventDtos =
                analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertEquals(analyticsBetweenInterval, analyticsEventDtos);
    }

    @Test
    void getAnalytics_shouldGetWhenHasNotInterval() {
        when(analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(analyticsFromDB);
        List<AnalyticsEventDto> analyticsEventDtos =
                analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

        assertEquals(analyticsBetweenFromAndTo, analyticsEventDtos);
    }

    @Test
    void handleLikeEventSaveSuccess() {
        LikeEvent event = LikeEvent.builder()
                .postId(1L)
                .authorId(1L)
                .userId(1L)
                .likedAt(LocalDateTime.parse("2024-04-01T12:00:00"))
                .type(faang.school.analytics.until.EventType.LIKED_POST)
                .build();

        // ВАЖНО: вызываем метод, который должен вызывать .save()
        analyticsEventService.handleLikeEvent(event);

        AnalyticsEvent expected = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.POST_LIKE)
                .receivedAt(LocalDateTime.parse("2024-04-01T12:00:00"))
                .build();

        verify(analyticsEventRepository, times(1)).save(expected);
    }
}