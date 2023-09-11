package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.analytics.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    public void getLatestEventsTest() {
        analyticsService.setLatestPeriodOfTime("1 day");

        analyticsService.getLatestEvents();

        Mockito.verify(analyticsEventRepository, Mockito.times(1)).getLatestEvents("1 day");
    }
}
