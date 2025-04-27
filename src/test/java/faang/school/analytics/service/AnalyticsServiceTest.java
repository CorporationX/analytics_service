package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsService analyticsService;

    private static final long RECEIVER_ID = 1L;
    private final EventType eventType = EventType.FOLLOWER;
    private final LocalDateTime fromDate = LocalDateTime.of(2025, 1, 1, 1, 1);
    private final LocalDateTime toDate = LocalDateTime.of(2025, 2, 1, 1, 1);
    private final AnalyticsEvent analyticsEvent = new AnalyticsEvent();

    @BeforeEach
    public void setUp() {
        analyticsEvent.setReceivedAt(LocalDateTime.of(2025, 1, 1, 1, 2));
    }

    @Test
    @DisplayName("Успешное получение аналитики по receiverId и eventType")
    public void givenAnalyticsEventExists_WhenGetAnalytics_ThenReturnAnalyticsEventDtoList() {
        Stream<AnalyticsEvent> analyticsEvents = Stream.of(analyticsEvent);
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, eventType)).thenReturn(analyticsEvents);
        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(new AnalyticsEventDto());

        List<AnalyticsEventDto> result = analyticsService.getAnalytics(RECEIVER_ID, eventType, fromDate, toDate);

        assertNotNull(result);
    }
}
