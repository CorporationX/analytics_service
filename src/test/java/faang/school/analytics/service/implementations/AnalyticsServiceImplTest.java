package faang.school.analytics.service.implementations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.EventTypeDto;
import faang.school.analytics.dto.GetAnalyticsRqDto;
import faang.school.analytics.dto.IntervalDto;
import faang.school.analytics.mapper.AnalyticsMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {
    private final long RECEIVER_ID = 10L;
    private final EventType EVENT_TYPE = EventType.POST_LIKE;
    private final LocalDateTime RECEIVED_AT_FIRST = LocalDateTime.of(2023, 10, 1, 0, 0, 0);
    private final LocalDateTime RECEIVED_AT_SECOND = LocalDateTime.of(2022, 9, 13, 0, 0, 0);
    private final LocalDateTime RECEIVED_AT_THIRD = LocalDateTime.of(2024, 12, 10, 0, 0, 0);
    private final AnalyticsEvent FIRST_EVENT = AnalyticsEvent.builder().id(1L).receiverId(RECEIVER_ID)
            .actorId(20L).eventType(EVENT_TYPE).receivedAt(RECEIVED_AT_FIRST).build();
    private final AnalyticsEvent SECOND_EVENT = AnalyticsEvent.builder().id(2L).receiverId(RECEIVER_ID)
            .actorId(20L).eventType(EVENT_TYPE).receivedAt(RECEIVED_AT_SECOND).build();
    private final AnalyticsEvent THIRD_EVENT = AnalyticsEvent.builder().id(3L).receiverId(RECEIVER_ID)
            .actorId(20L).eventType(EVENT_TYPE).receivedAt(RECEIVED_AT_THIRD).build();

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsMapperImpl analyticsMapper;
    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @ParameterizedTest
    @CsvSource({"month, 1, 1", "year, 2, 2", "year, 3, 3"})
    public void testGeneratePeriodWithInterval(String interval, int count, int expectedResult) {
        GetAnalyticsRqDto analyticsRqDto = new GetAnalyticsRqDto(RECEIVER_ID, EventTypeDto.POST_LIKE,
                IntervalDto.of(interval), count, null, null);
        when(analyticsEventRepository.findByReceiverIdAndEventType(analyticsRqDto.receiverId(), analyticsRqDto.eventTypeDto()
                .toEventType())).thenReturn(Stream.of(FIRST_EVENT, SECOND_EVENT, THIRD_EVENT));

        List<AnalyticsEventDto> result = analyticsService.getAnalytics(analyticsRqDto);

        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(analyticsRqDto.receiverId(),
                analyticsRqDto.eventTypeDto().toEventType());
        assertThat(result.size()).isEqualTo(expectedResult);
    }

    @Test
    public void testComparatorInGeneratePeriod() {
        GetAnalyticsRqDto analyticsRqDto = new GetAnalyticsRqDto(RECEIVER_ID, EventTypeDto.POST_LIKE, IntervalDto.YEAR, 2, null, null);
        when(analyticsEventRepository.findByReceiverIdAndEventType(analyticsRqDto.receiverId(), analyticsRqDto.eventTypeDto()
                .toEventType())).thenReturn(Stream.of(FIRST_EVENT, SECOND_EVENT, THIRD_EVENT));

        List<AnalyticsEventDto> result = analyticsService.getAnalytics(analyticsRqDto);

        assertThat(result.get(0).id()).isEqualTo(3);
    }
}