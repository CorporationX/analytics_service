package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.impl.AnalyticsDateRangeFilter;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AnalyticsDateFilterTest {

    private AnalyticsDateRangeFilter dateRangeFilter;
    private AnalyticsFilterDto filterDto;
    private AnalyticsEvent event;

    @BeforeEach
    public void setUp() {
        dateRangeFilter = new AnalyticsDateRangeFilter();
        filterDto = new AnalyticsFilterDto();
        event = new AnalyticsEvent();
    }

    @Test
    public void testIsApplicable_true() {
        filterDto.setFrom(LocalDateTime.now().minusHours(1));
        filterDto.setTo(LocalDateTime.now().plusHours(1));
        Assertions.assertEquals(true, dateRangeFilter.isApplicable(filterDto));
    }

    @Test
    public void testIsApplicable_false() {
        Assertions.assertEquals(false, dateRangeFilter.isApplicable(filterDto));
    }

    @Test
    public void testAction_true() {
        filterDto.setFrom(LocalDateTime.now().minusHours(1));
        filterDto.setTo(LocalDateTime.now().plusHours(1));
        event.setReceivedAt(LocalDateTime.now());
        Assertions.assertEquals(true, dateRangeFilter.action(event,filterDto));
    }

    @Test
    public void testAction_false() {
        filterDto.setFrom(LocalDateTime.now().minusHours(1));
        filterDto.setTo(LocalDateTime.now().plusHours(1));
        event.setReceivedAt(LocalDateTime.now().plusHours(2));
        Assertions.assertEquals(false, dateRangeFilter.action(event,filterDto));
    }
}
