package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    private final long analyticsEventId = 1L;
    private final long receiverId = 10L;
    private final EventType eventType = EventType.FOLLOWER;
    private AnalyticsEvent analyticsEvent;
    private AnalyticsEvent firstAnalyticsEvent;
    private AnalyticsEvent secondAnalyticsEvent;

    @BeforeEach
    void init () {
        analyticsEvent = AnalyticsEvent.builder()
                .id(analyticsEventId)
                .receiverId(receiverId)
                .build();

        firstAnalyticsEvent = AnalyticsEvent.builder()
                .id(5L)
                .receiverId(receiverId)
                .receivedAt(LocalDateTime.now())
                .eventType(eventType)
                .build();

        secondAnalyticsEvent = AnalyticsEvent.builder()
                .id(6L)
                .receiverId(receiverId)
                .eventType(eventType)
                .build();
    }

    @Test
    void testSave() {
        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testDelete() {
        analyticsEventService.deleteEvent(analyticsEventId);

        verify(analyticsEventRepository, times(1)).deleteById(analyticsEventId);
    }

    @Test
    void testGetById_eventNotExist_throwsEntityNotFoundException() {
        long wrongId = analyticsEventId + 1;
        when(analyticsEventRepository.findById(wrongId)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> analyticsEventService.getById(wrongId)
        );
    }

    @Test
    void testGetById_eventExist_returnEvent() {
        when(analyticsEventRepository.findById(analyticsEventId)).thenReturn(Optional.of(analyticsEvent));

        AnalyticsEvent analyticsEventById = analyticsEventService.getById(analyticsEventId);

        assertEquals(analyticsEvent, analyticsEventById);
    }

    @Test
    void testGetAnalytics_intervalIsNull() {
        Interval interval = null;
        LocalDateTime from = LocalDateTime.MIN;
        LocalDateTime to = LocalDateTime.of(2020, 1, 1, 0, 0);
        firstAnalyticsEvent.setReceivedAt(to.minusYears(2));//in interval, 2 years earlier
        secondAnalyticsEvent.setReceivedAt(to.plusYears(2));//not in interval, 2 years later
        Stream<AnalyticsEvent> analyticsEvents = Stream.of(firstAnalyticsEvent, secondAnalyticsEvent);

        AnalyticsEventDto expectedAnalyticsEventDto = analyticsEventMapper.toDto(firstAnalyticsEvent);
        List<AnalyticsEventDto> expectedAnalyticsEventDtos = Collections.singletonList(expectedAnalyticsEventDto);
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(analyticsEvents);

        List<AnalyticsEventDto> analyticsByService = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertEquals(expectedAnalyticsEventDtos.size(), analyticsByService.size());
        assertEquals(expectedAnalyticsEventDtos.get(0).getId(), analyticsByService.get(0).getId());
    }

    @Test
    void testGetAnalytics_intervalIsNotNull() {
        Interval interval = Interval.YEAR;
        firstAnalyticsEvent.setReceivedAt(LocalDateTime.now().minusMonths(6));//in interval, middle of the same year
        secondAnalyticsEvent.setReceivedAt(LocalDateTime.MIN);//not in interval
        Stream<AnalyticsEvent> analyticsEvents = Stream.of(firstAnalyticsEvent, secondAnalyticsEvent);

        AnalyticsEventDto expectedAnalyticsEventDto = analyticsEventMapper.toDto(firstAnalyticsEvent);
        List<AnalyticsEventDto> expectedAnalyticsEventDtos = Collections.singletonList(expectedAnalyticsEventDto);
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(analyticsEvents);

        List<AnalyticsEventDto> analyticsByService = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, null, null);

        assertEquals(expectedAnalyticsEventDtos.size(), analyticsByService.size());
        assertEquals(expectedAnalyticsEventDtos.get(0).getId(), analyticsByService.get(0).getId());
    }
}