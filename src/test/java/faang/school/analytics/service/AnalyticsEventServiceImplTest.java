package faang.school.analytics.service;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.like.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.analytics.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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

    LikeEvent likeEvent;
    AnalyticsEvent analyticsEvent;
    LocalDateTime date;

    Long authorId = 1L;
    Long postId = 1L;
    Long userId = 1L;

    @BeforeEach
    void setup() {
        date = LocalDateTime.now();

        likeEvent = LikeEvent.builder()
                .postId(postId)
                .authorId(authorId)
                .userId(userId)
                .createdAt(date)
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(postId)
                .actorId(userId)
                .eventType(EventType.POST_LIKE)
                .receivedAt(date)
                .build();
    }

    @Test
    public void addLikeEventTest() {
        analyticsEventService.addLikeEvent(likeEvent);

        verify(analyticsEventMapper, times(1)).likeEventToAnalytics(likeEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }
}
