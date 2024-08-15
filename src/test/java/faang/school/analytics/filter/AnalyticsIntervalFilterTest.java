package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.impl.AnalyticsIntervalFilter;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AnalyticsIntervalFilterTest {

    private AnalyticsIntervalFilter filter;
    private AnalyticsFilterDto filterDto;
    private AnalyticsEvent event;

    @BeforeEach
    public void setUp() {
        filter = new AnalyticsIntervalFilter();
        filterDto = new AnalyticsFilterDto();
        event = new AnalyticsEvent();
    }

    @Test
    public void testIsApplicable_true() {
        filterDto.setInterval(Interval.DAILY);
        Assertions.assertEquals(true, filter.isApplicable(filterDto));
    }

    @Test
    public void testIsApplicable_false() {
        Assertions.assertEquals(false, filter.isApplicable(filterDto));
    }

    @Test
    public void testAction_true() {
        event.setReceivedAt(LocalDateTime.now());
        filterDto.setInterval(Interval.HOURLY);
        Assertions.assertEquals(true, filter.action(event, filterDto));
    }

    @Test
    public void testAction_false() {
        event.setReceivedAt(LocalDateTime.now());
        filterDto.setInterval(Interval.OTHER);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> filter.action(event, filterDto));
    }
}
