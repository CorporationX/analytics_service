package faang.school.analytics.service;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @InjectMocks
    AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    AnalyticsEventRepository analyticsEventRepository;

    LikeEvent likeEvent = new LikeEvent();
    AnalyticsEvent analyticsEvent = new AnalyticsEvent();

    Long entityId = 1L;
    Long eventTypeId = 5L;
    EventType eventType = EventType.POST_LIKE;
    Long intervalId = 1L;
    String startDateTime = LocalDateTime.now().toString();
    String endDateTime  = LocalDateTime.now().plusDays(3).toString();

    @BeforeEach
    void setUp() {
        likeEvent.setPostId(1L);
        likeEvent.setUserId(1L);
        likeEvent.setCreatedAt(LocalDateTime.now());

        analyticsEvent.setEventType(EventType.POST_LIKE);
        analyticsEvent.setActorId(1L);
        analyticsEvent.setReceiverId(1L);
        analyticsEvent.setReceivedAt(LocalDateTime.now());
    }

    @Test
    void testLikeEventSave() {
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testGetAnalyticOfEvent_Success() {
        AnalyticsEvent mockEvent = mock(AnalyticsEvent.class);
        doReturn(LocalDateTime.now().plusDays(1)).when(mockEvent).getReceivedAt();
        when(analyticsEventRepository.findByReceiverIdAndEventType(entityId, eventType))
                .thenAnswer(invocation -> Stream.of(mockEvent));
        analyticsEventService.getAnalyticOfEvent(entityId, eventTypeId, intervalId, startDateTime, endDateTime);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(entityId, eventType);
    }

    @Test
    void testGetAnalyticOfEvent_timeException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Long newIntervalId = 16L;
            String newStartDateTime = null;
            analyticsEventService.getAnalyticOfEvent(entityId, eventTypeId, newIntervalId, newStartDateTime, endDateTime);
        });
    }

    @Test
    void testGetAnalyticOfEvent_EntityExistsException() {
        Assert.assertThrows(EntityExistsException.class, () -> {
            when(analyticsEventRepository.findByReceiverIdAndEventType(entityId, eventType))
                    .thenAnswer(invocation -> Stream.of());
            analyticsEventService.getAnalyticOfEvent(entityId, eventTypeId, intervalId, startDateTime, endDateTime);
        });
    }

}
