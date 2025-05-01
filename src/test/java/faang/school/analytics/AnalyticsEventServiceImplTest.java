package faang.school.analytics;

import faang.school.analytics.dto.AggregatedAnalyticDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsGetDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import faang.school.analytics.model.Interval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Test
    public void saveAnalyticsShouldSaveAndReturnDto() {
        AnalyticsEventDto inputDto = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(100L)
                .actorId(200L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.now())
                .build();

        AnalyticsEvent savedEntity = new AnalyticsEvent();
        savedEntity.setId(1L);
        savedEntity.setReceiverId(100L);
        savedEntity.setActorId(200L);
        savedEntity.setEventType(EventType.PROFILE_VIEW);
        savedEntity.setReceivedAt(inputDto.getReceivedAt());

        Mockito.when(analyticsEventRepository.save(Mockito.any(AnalyticsEvent.class))).thenReturn(savedEntity);

        AnalyticsEventDto result = analyticsEventService.saveAnalytics(inputDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getReceiverId()).isEqualTo(100L);
        verify(analyticsEventRepository).save(Mockito.any(AnalyticsEvent.class));
    }

    @Test
    void getAnalyticsShouldGroupEventsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsEvent event1 = new AnalyticsEvent();
        event1.setId(1L);
        event1.setReceivedAt(now);
        event1.setEventType(EventType.TASK_COMPLETED);
        event1.setReceiverId(1L);

        AnalyticsEvent event2 = new AnalyticsEvent();
        event2.setId(2L);
        event2.setReceivedAt(now.plusHours(1));
        event2.setEventType(EventType.TASK_COMPLETED);
        event2.setReceiverId(1L);
        AnalyticsGetDto analyticsGetDto = AnalyticsGetDto.builder()
                .receiverId(1L)
                .eventType(EventType.TASK_COMPLETED)
                .interval(Interval.HOUR)
                .from(now.minusHours(1))
                .to(now.plusHours(2)).build();

        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), Mockito.any(EventType.class)))
                .thenReturn(Stream.of(event1, event2));

        List<AggregatedAnalyticDto> result = analyticsEventService.getAnalytics(analyticsGetDto);

        assertThat(result).hasSize(2);
        verify(analyticsEventMapper, times(0)).toDto(Mockito.any());
    }
}
