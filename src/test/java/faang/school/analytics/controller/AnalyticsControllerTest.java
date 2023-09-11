package faang.school.analytics.controller;


import faang.school.analytics.service.analytics.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnalyticsControllerTest {
    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks AnalyticsController analyticsController;

    @Test
    public void getLatestEventsTest() {
        analyticsService.setLatestPeriodOfTime("1 day");

        analyticsController.getLatestEvents();
        Mockito.verify(analyticsService, Mockito.times(1)).getLatestEvents();
    }
}
