package faang.school.analytics.service;


import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);


    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @BeforeEach
    public void setUp() {
        analyticsEventService = new AnalyticsEventService(analyticsEventRepository, analyticsEventMapper);
    }

    @Test
    public void testSaveEvent() {
        AnalyticsEvent event = new AnalyticsEvent();
        analyticsEventService.saveEvent(event);

        verify(analyticsEventRepository).save(event);
    }

    @ParameterizedTest
    @CsvSource({
            "1, POST_COMMENT, HOUR",
            "2, POST_LIKE, DAY",
            "3, SKILL_RECEIVED, WEEK"
    })
    public void getAnalyticsEventByIntervalParameterizedTest(long receiverId, String eventType, String interval) {
        EventType eventTypeEnum = EventType.valueOf(eventType);
        Interval intervalEnum = Interval.valueOf(interval);

        List<AnalyticsEvent> mockEvents = new ArrayList<>();

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        mockEvents.add(createAnalyticsEvent(3L, receiverId, eventTypeEnum, oneWeekAgo));
        mockEvents.add(createAnalyticsEvent(2L, receiverId, eventTypeEnum, LocalDateTime.now()));

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventTypeEnum)).thenReturn(mockEvents.stream());

        List<AnalyticsEventDto> analytics = analyticsEventService.getAnalytics(receiverId, eventTypeEnum, intervalEnum, null, null);
        analytics.forEach(System.out::println);
        assertThat(analytics).hasSize(1);
        assertThat(analytics.get(0).eventType()).isEqualTo(eventType);
    }

    @Test
    public void testGetAnalyticsWithDateSuccessTest() {
        long receiverId = 1L;
        EventType eventType = EventType.ACHIEVEMENT_RECEIVED;
        LocalDateTime from = LocalDateTime.now().minusHours(2);
        LocalDateTime to = LocalDateTime.now();

        List<AnalyticsEvent> mockEvents = new ArrayList<>();
        mockEvents.add(createAnalyticsEvent(1L, receiverId, eventType, LocalDateTime.now().minusHours(1)));
        mockEvents.add(createAnalyticsEvent(2L, receiverId, eventType, to));

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(mockEvents.stream());

        List<AnalyticsEventDto> analytics = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

        assertThat(analytics).hasSize(1);
        assertThat(analytics.get(0).eventType()).isEqualTo("ACHIEVEMENT_RECEIVED");
    }

    @Test
    public void testGetAnalyticsEventsNotFoundSuccessTest() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        LocalDateTime from = LocalDateTime.now().minusHours(2);
        LocalDateTime to = LocalDateTime.now();

        List<AnalyticsEvent> mockEvents = new ArrayList<>();
        mockEvents.add(createAnalyticsEvent(1L, receiverId, eventType, LocalDateTime.now()));

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(mockEvents.stream());

        List<AnalyticsEventDto> analytics = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

        assertThat(analytics).isEmpty();
    }

    private AnalyticsEvent createAnalyticsEvent(long id, long receiverId, EventType eventType, LocalDateTime receivedAt) {
        AnalyticsEvent event = new AnalyticsEvent();
        event.setId(id);
        event.setReceiverId(receiverId);
        event.setEventType(eventType);
        event.setReceivedAt(receivedAt);
        return event;
    }
}