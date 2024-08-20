package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void saveEvent_ShouldReturnSavedEventDto() {
        AnalyticsEvent event = new AnalyticsEvent();
        AnalyticsEventDto eventDto = new AnalyticsEventDto();
        when(analyticsEventRepository.save(any(AnalyticsEvent.class))).thenReturn(event);
        when(analyticsEventMapper.analyticsEventToAnalyticsEventDto(event)).thenReturn(eventDto);

        AnalyticsEventDto result = analyticsEventService.saveEvent(event);

        assertEquals(eventDto, result);
        verify(analyticsEventRepository).save(event);
        verify(analyticsEventMapper).analyticsEventToAnalyticsEventDto(event);
    }
}
