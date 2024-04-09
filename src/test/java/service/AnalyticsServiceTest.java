package service;

import faang.school.analytics.event.postview.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    AnalyticsService analyticsService;

    private PostViewEvent postViewEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setup() {
        postViewEvent = PostViewEvent.builder()
                .postId(1L)
                .authorId(10L)
                .userId(100L)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(postViewEvent.getUserId())
                .receiverId(postViewEvent.getAuthorId())
                .receivedAt(postViewEvent.getViewedAt())
                .build();
    }

    @Test
    void savePostViewEvent_PostViewEventMappedToAnalyticsEvent_ThenSavedToDb() {
        analyticsService.savePostViewEvent(postViewEvent);

        assertAll(
                () -> verify(analyticsEventRepository, times(1)).save(any(AnalyticsEvent.class)),
                () -> verify(analyticsEventMapper, times(1)).toAnalyticsEvent(any(PostViewEvent.class))
        );
    }
}
