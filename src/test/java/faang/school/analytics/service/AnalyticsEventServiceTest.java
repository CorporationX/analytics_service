package faang.school.analytics.service;

import faang.school.analytics.AnalyticsEventMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsEventService eventService;

    @Test
    void testSave() {
        AnalyticsEventDto eventDto = new AnalyticsEventDto();
        AnalyticsEvent event = new AnalyticsEvent();

        when(analyticsEventMapper.toEntity(eventDto)).thenReturn(event);
        eventService.save(eventDto);

        verify(analyticsEventMapper).toEntity(eventDto);
        verify(analyticsEventRepository).save(event);
    }
}