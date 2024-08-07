package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.NotFoundException;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    AnalyticsEventRepository analyticsEventRepository;

    @Spy
    AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    AnalyticsEventService analyticsEventService;

    long testReceiverId = 1L;
    EventType testEventType = EventType.POST_VIEW;
    Interval testInterval = Interval.MONTH;
    AnalyticsEvent testAnalyticsEvent = new AnalyticsEvent();
    LocalDateTime fixTime = LocalDateTime.of(2024, 8, 5, 12, 0);
    LocalDateTime from = fixTime.minusMonths(4);
    LocalDateTime to = fixTime.minusMonths(1);

    public List<AnalyticsEvent> prepareListAnalyticsEvent() {
        AnalyticsEvent firstAnalyticsEvent = new AnalyticsEvent();
        firstAnalyticsEvent.setId(1L);
        firstAnalyticsEvent.setReceivedAt(fixTime.minusDays(10));
        AnalyticsEvent secondAnalyticsEvent = new AnalyticsEvent();
        secondAnalyticsEvent.setId(2L);
        secondAnalyticsEvent.setReceivedAt(fixTime.minusDays(5));
        AnalyticsEvent thirdAnalyticsEvent = new AnalyticsEvent();
        thirdAnalyticsEvent.setId(3L);
        thirdAnalyticsEvent.setReceivedAt(fixTime.minusDays(1));
        AnalyticsEvent fourthAnalyticsEvent = new AnalyticsEvent();
        fourthAnalyticsEvent.setId(4L);
        fourthAnalyticsEvent.setReceivedAt(fixTime.minusMonths(2));
        AnalyticsEvent fifthAnalyticsEvent = new AnalyticsEvent();
        fifthAnalyticsEvent.setId(5L);
        fifthAnalyticsEvent.setReceivedAt(fixTime.minusWeeks(5));
        AnalyticsEvent sixthAnalyticsEvent = new AnalyticsEvent();
        sixthAnalyticsEvent.setId(6L);
        sixthAnalyticsEvent.setReceivedAt(fixTime.minusHours(10));
        return List.of(firstAnalyticsEvent, secondAnalyticsEvent, thirdAnalyticsEvent,
                fourthAnalyticsEvent, fifthAnalyticsEvent, sixthAnalyticsEvent);
    }

    @Test
    void testSaveEventIsSuccessful() {

        analyticsEventService.saveEvent(testAnalyticsEvent);

        verify(analyticsEventRepository, times(1)).save(testAnalyticsEvent);
    }

    @Test
    void testGetAnalyticsIfAnalyticsEventNotFound() {
//        long testReceiverId = 1L;
//        EventType testEventType = EventType.POST_VIEW;
//        Interval testInterval = Interval.DAY;
        when(analyticsEventRepository
                .findByReceiverIdAndEventType(testReceiverId, testEventType))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> analyticsEventService.getAnalytics(testReceiverId, testEventType,
                testInterval, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void testGetAnalyticsIfIntervalNotNull() {
//        long testReceiverId = 1L;
//        EventType testEventType = EventType.POST_VIEW;
//        Interval testInterval = Interval.MONTH;
        List<AnalyticsEvent> testList = prepareListAnalyticsEvent();
        Stream<AnalyticsEvent> testStream = testList.stream();
        AnalyticsEventDto firstDto = new AnalyticsEventDto();
        firstDto.setId(1L);
        firstDto.setReceivedAt(fixTime.minusDays(10));
        AnalyticsEventDto secondDto = new AnalyticsEventDto();
        secondDto.setId(2L);
        secondDto.setReceivedAt(fixTime.minusDays(5));
        AnalyticsEventDto thirdDto = new AnalyticsEventDto();
        thirdDto.setId(3L);
        thirdDto.setReceivedAt(fixTime.minusDays(1));
        AnalyticsEventDto fourthDto = new AnalyticsEventDto();
        fourthDto.setId(6L);
        fourthDto.setReceivedAt(fixTime.minusHours(10));
        List<AnalyticsEventDto> actualDtoList = List.of(firstDto, secondDto, thirdDto, fourthDto);
        when(analyticsEventRepository.findByReceiverIdAndEventType(testReceiverId, testEventType)).thenReturn(testStream);

        List<AnalyticsEventDto> resultDtoList =
                analyticsEventService.getAnalytics(testReceiverId, testEventType,
                        testInterval, LocalDateTime.now(), LocalDateTime.now());

        assertEquals(resultDtoList, actualDtoList);
    }

    @Test
    void testGetAnalyticsIfIntervalIsNull() {
//        long testReceiverId = 1L;
//        EventType testEventType = EventType.POST_VIEW;
//        LocalDateTime from = fixTime.minusMonths(4);
//        LocalDateTime to = fixTime.minusMonths(1);
        List<AnalyticsEvent> testList = prepareListAnalyticsEvent();
        Stream<AnalyticsEvent> testStream = testList.stream();
        AnalyticsEventDto firstDto = new AnalyticsEventDto();
        firstDto.setId(4L);
        firstDto.setReceivedAt(fixTime.minusMonths(2));
        AnalyticsEventDto secondDto = new AnalyticsEventDto();
        secondDto.setId(5L);
        secondDto.setReceivedAt(fixTime.minusWeeks(5));
        List<AnalyticsEventDto> actualDtoList = List.of(firstDto, secondDto);
        when(analyticsEventRepository.findByReceiverIdAndEventType(testReceiverId, testEventType)).thenReturn(testStream);

        List<AnalyticsEventDto> resultDtoList =
                analyticsEventService.getAnalytics(testReceiverId, testEventType,
                        null, from, to);

        assertEquals(resultDtoList, actualDtoList);
    }

    @Test
    void testGetAnalyticsSuccessful() {
//        long testReceiverId = 1L;
//        EventType testEventType = EventType.POST_VIEW;
//        Interval testInterval = Interval.DAY;

        analyticsEventService.getAnalytics(testReceiverId, testEventType, testInterval, LocalDateTime.now(), LocalDateTime.now());

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(testReceiverId, testEventType);
    }

}
