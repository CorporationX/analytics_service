package faang.school.analytics.controller;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.ConvertAnalyticsParam;
import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventControllerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventValidator analyticsEventValidator;
    @Mock
    private ConvertAnalyticsParam convertAnalyticsParam;

    @InjectMocks
    private AnalyticsEventController analyticsEventController;

    @Test
    void testGetAnalytics() {
        // Arrange
        long recieverId = 1L;
        String event = "POST_COMMENT";
        String interval = "WEEK";
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        Interval intervalConvert = Interval.WEEK;
        LocalDateTime fromConvert = LocalDateTime.now().minusDays(7);
        LocalDateTime toConvert = LocalDateTime.now();

        when(convertAnalyticsParam.convertInterval(interval)).thenReturn(intervalConvert);
        when(convertAnalyticsParam.convertFromDate(from)).thenReturn(Optional.of(fromConvert));
        when(convertAnalyticsParam.convertToDate(to)).thenReturn(Optional.of(toConvert));

        // Act
        analyticsEventController.getAnalytics(recieverId, event, interval, from, to);

        // Assert
        verify(analyticsEventValidator, times(1)).validateRequest(interval, from, to);
        verify(convertAnalyticsParam, times(1)).convertInterval(interval);
        verify(convertAnalyticsParam, times(1)).convertFromDate(from);
        verify(convertAnalyticsParam, times(1)).convertToDate(to);
        verify(analyticsEventService, times(1))
                .getAnalytics(recieverId, EventType.valueOf(event), intervalConvert, fromConvert, toConvert);
    }
}