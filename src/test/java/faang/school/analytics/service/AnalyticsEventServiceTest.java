package faang.school.analytics.service;

import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.dto.event.ResponseAnalyticsEventDto;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    private final LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeEach
    void initData() {
        Stream<AnalyticsEvent> analyticsEventList = Stream.of(
                AnalyticsEvent.builder()
                        .id(1)
                        .actorId(1)
                        .receiverId(1)
                        .eventType(EventType.POST_COMMENT)
                        .receivedAt(localDateTime.minusDays(2))
                        .build(),
                AnalyticsEvent.builder()
                        .id(2)
                        .actorId(1)
                        .receiverId(1)
                        .receivedAt(localDateTime.minusMonths(1))
                        .build(),
                AnalyticsEvent.builder()
                        .id(3)
                        .actorId(1)
                        .receiverId(1)
                        .eventType(EventType.POST_COMMENT)
                        .receivedAt(localDateTime)
                        .build(),
                AnalyticsEvent.builder()
                        .id(4)
                        .actorId(1)
                        .receiverId(1)
                        .eventType(EventType.POST_COMMENT)
                        .receivedAt(localDateTime.minusMinutes(1))
                        .build(),
                AnalyticsEvent.builder()
                        .id(5)
                        .actorId(1)
                        .receiverId(1)
                        .eventType(EventType.POST_COMMENT)
                        .receivedAt(localDateTime.minusMinutes(5))
                        .build()
        );

        when(analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.POST_COMMENT))
                .thenReturn(analyticsEventList);
    }

    @Test
    public void testGetAnalyticsWithInterval() {
        List<ResponseAnalyticsEventDto> responseAnalyticsEventDtoList = analyticsEventService
                .getAnalytics(1, EventType.POST_COMMENT, Interval.TODAY, null, null);
        responseAnalyticsEventDtoList.forEach(System.out::println);

        assertEquals(3, responseAnalyticsEventDtoList.size());
    }

    @Test
    public void testGetAnalyticsWithDates() {
        List<ResponseAnalyticsEventDto> responseAnalyticsEventDtoList = analyticsEventService
                .getAnalytics(1, EventType.POST_COMMENT, null, localDateTime, localDateTime.plusDays(1L));

        assertEquals(1, responseAnalyticsEventDtoList.size());
    }
}
