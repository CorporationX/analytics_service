package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.mapper.LikeEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.Assertions;
import faang.school.analytics.validator.AnalyticsEventServiceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventValidator analyticsEventValidator;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Spy
    private LikeEventMapperImpl likeEventMapper;

    @Mock
    private AnalyticsEventServiceValidator analyticsEventServiceValidator;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    long receiverId = 1L;
    EventType eventType = EventType.FOLLOWER;
    AnalyticsEvent event1 = AnalyticsEvent.builder()
            .eventType(eventType)
            .receiverId(receiverId)
            .receivedAt(LocalDateTime.now())
            .build();
    AnalyticsEvent event2 = AnalyticsEvent.builder()
            .eventType(eventType)
            .receiverId(receiverId)
            .receivedAt(LocalDateTime.now().minusHours(1))
            .build();

    @Test
    public void testSaveEvent() {
        AnalyticsEvent event = new AnalyticsEvent();
        Mockito.when(analyticsEventRepository.save(Mockito.any())).thenReturn(event);
        analyticsEventService.saveEvent(event);
        Mockito.verify(analyticsEventRepository).save(Mockito.any());
    }

    @Test
    public void testGetAnalyticsWithInterval() {
        Interval interval = Interval.HOUR;

        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event1, event2));

        List<AnalyticsEventDto> expected = List.of(analyticsEventMapper.toDto(event1));
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType.name(), interval.name().describeConstable(), Optional.empty(), Optional.empty());

        Assertions.assertEquals(result, expected);
        Assertions.assertEquals(expected.size(), result.size());
    }

    @Test
    public void testGetAnalyticsWithoutInterval() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(LocalDateTime.now().minusHours(1).format(formatter));
        LocalDateTime endDate = LocalDateTime.parse(LocalDateTime.now().format(formatter));

        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event1, event2));

        List<AnalyticsEventDto> expected = List.of(analyticsEventMapper.toDto(event1));
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType.name(), Optional.empty(), startDate.toString().describeConstable(), endDate.toString().describeConstable());

        Assertions.assertEquals(result, expected);
        Assertions.assertEquals(expected.size(), result.size());
    }


    @Test
    void saveLikeEventTest() {
        LikeEvent likeEvent = new LikeEvent();
        likeEvent.setPostId(123L);
        likeEvent.setUserId(789L);
        likeEvent.setReceivedAt(LocalDateTime.parse("2024-08-16T12:00:00"));
        likeEvent.setEventType(EventType.of(5));

        AnalyticsEvent analyticsEvent = likeEventMapper.toEntity(likeEvent);

        analyticsEventService.saveLikeEvent(likeEvent);

        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
