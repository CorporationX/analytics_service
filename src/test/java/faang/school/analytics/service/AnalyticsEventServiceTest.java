package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.PostViewEventMapper;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private PostViewEventMapper postViewEventMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    private PostViewEventDto eventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void initData() {
        eventDto = PostViewEventDto.builder()
                .authorId(1L)
                .postId(1L)
                .userId(1L)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(1L)
                .receiverId(1L)
                .eventType(EventType.POST_VIEW)
                .build();
    }

    @Test
    void testSaveEvent() {
        when(postViewEventMapper.toModel(eventDto)).thenReturn(analyticsEvent);
        analyticsEventService.saveEvent(eventDto);

        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
