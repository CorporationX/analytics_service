package faang.school.analytics.service;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.enums.EventType;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {
    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    @Test
    void testSaveCommentEvent() {
        CommentEvent commentEvent = new CommentEvent();

        analyticsEventService.saveCommentEvent(commentEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEventCaptor.capture());
        assertEquals(EventType.POST_COMMENT, analyticsEventCaptor.getValue().getEventType());
    }
}