package faang.school.analytics.service;

import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.mapper.FollowEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    @Mock
    FollowEventMapper followEventMapper;
    @InjectMocks
    AnalyticsEventService analyticsEventService;

    AnalyticsEvent analyticsEvent;

    @Test
    void FollowEventSaveTest() {
        analyticsEvent = new AnalyticsEvent();
        FollowEventDto followEventDto = new FollowEventDto();
        Mockito.when(followEventMapper.toEntity(Mockito.any())).thenReturn(analyticsEvent);
        analyticsEventService.followEventSave(followEventDto);
        Mockito.verify(analyticsEventRepository, Mockito.times(1)).save(analyticsEvent);
    }
}
