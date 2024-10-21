package faang.school.analytics.service;

import faang.school.analytics.model.Interval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class IntervalServiceTest {
    @InjectMocks
    private IntervalService intervalService;

    private static final LocalDateTime NOW = LocalDateTime.now();

    @Test
    void testGetFrom_lastDay() {
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime result = intervalService.getFrom(Interval.LAST_DAY);

        assertEquals(expected, result);
    }

    @Test
    void testGetFrom_lastWeek() {
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().minusWeeks(1), LocalTime.MIN);
        LocalDateTime result = intervalService.getFrom(Interval.LAST_WEEK);

        assertEquals(expected, result);
    }

    @Test
    void testGetFrom_lastMonth() {
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.MIN);
        LocalDateTime result = intervalService.getFrom(Interval.LAST_MONTH);

        assertEquals(expected, result);
    }

    @Test
    void testGetFrom_lastThreeMonths() {
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().minusMonths(3), LocalTime.MIN);
        LocalDateTime result = intervalService.getFrom(Interval.LAST_THREE_MONTHS);

        assertEquals(expected, result);
    }

    @Test
    void testGetFrom_lastSixMonths() {
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().minusMonths(6), LocalTime.MIN);
        LocalDateTime result = intervalService.getFrom(Interval.LAST_SIX_MONTHS);

        assertEquals(expected, result);
    }

    @Test
    void testGetFrom_yearToDate() {
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0);
        LocalDateTime result = intervalService.getFrom(Interval.YEAR_TO_DATE);

        assertEquals(expected, result);
    }

    @Test
    void testGetFrom_allTime() {
        LocalDateTime expected = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime result = intervalService.getFrom(Interval.ALL_TIME);

        assertEquals(expected, result);
    }

    @Test
    void testGetTo() {
        LocalDateTime result = intervalService.getTo();
        assertTrue(result.isAfter(NOW.minusSeconds(1)) && result.isBefore(NOW.plusSeconds(1)));
    }
}