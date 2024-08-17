package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.LikeEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventServiceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventServiceValidator analyticsEventServiceValidator;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void saveLikeEventTest() {
        String messageContent = "123,456,789,2024-08-16T12:00:00";
        Message message = mock(Message.class);
        when(message.toString()).thenReturn(messageContent);

        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        when(analyticsEventMapper.likeEventToAnalyticsEvent(any(LikeEvent.class))).thenReturn(analyticsEvent);

        analyticsEventService.saveLikeEvent(message);

        ArgumentCaptor<LikeEvent> likeEventCaptor = ArgumentCaptor.forClass(LikeEvent.class);
        verify(analyticsEventMapper).likeEventToAnalyticsEvent(likeEventCaptor.capture());

        LikeEvent capturedLikeEvent = likeEventCaptor.getValue();

        assertEquals(123L, capturedLikeEvent.getPostId());
        assertEquals(456L, capturedLikeEvent.getAuthorId());
        assertEquals(789L, capturedLikeEvent.getUserId());
        assertEquals(LocalDateTime.parse("2024-08-16T12:00:00"), capturedLikeEvent.getTimestamp());

        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
