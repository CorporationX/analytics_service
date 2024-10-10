package faang.school.analytics;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @InjectMocks
    AnalyticsEventService analyticsEventService;

    @Mock
    AnalyticsEventRepository analyticsEventRepository;

    @Spy
    AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    LikeEvent likeEvent = new LikeEvent();
    AnalyticsEvent analyticsEvent = new AnalyticsEvent();

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
        when(analyticsEventMapper.toEntity(likeEvent)).thenReturn(analyticsEvent);
        analyticsEventService.saveLikeEvent(likeEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

}
