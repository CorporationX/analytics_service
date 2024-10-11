package faang.school.analytics.service;

import static org.junit.jupiter.api.Assertions.*;

import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticMapper;
import faang.school.analytics.mapper.AnalyticMapperImpl;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticMapperImpl analyticMapper;

    @InjectMocks
    private AnalyticServiceImpl analyticService;

    private ProjectViewEvent projectViewEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp() {
        projectViewEvent = new ProjectViewEvent(2L, 1L, LocalDateTime.now());
        analyticsEvent = new AnalyticsEvent();
    }

    @Test
    public void saveAnalyticEvent_shouldSaveAnalyticsEvent() {
        when(analyticMapper.toEntity(projectViewEvent)).thenReturn(analyticsEvent);

        analyticService.saveAnalyticEvent(projectViewEvent);

        assertEquals(EventType.PROJECT_VIEW, analyticsEvent.getEventType());
        verify(analyticMapper).toEntity(projectViewEvent);
        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
