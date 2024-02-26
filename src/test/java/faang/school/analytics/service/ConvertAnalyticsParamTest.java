package faang.school.analytics.service;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.Interval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConvertAnalyticsParamTest {

    private ConvertAnalyticsParam convertAnalyticsParam = new ConvertAnalyticsParam();
    private LocalDate date = LocalDate.now();
    private LocalDate dateNull = null;

    @Test
    void testConvertInterval_whenIntervalIsCorrect_thenConvertToEnum() {
        // Arrange
        String interval = "WEEK";
        // Act
        Interval result = convertAnalyticsParam.convertInterval(interval);
        // Assert
        assertEquals(result, Interval.WEEK);
    }

    @Test
    void testConvertInterval_whenIntervalIsNull_thenReturnNull() {
        // Arrange
        String interval = null;
        // Act
        Interval result = convertAnalyticsParam.convertInterval(interval);
        // Assert
        assertEquals(result, null);
    }

    @Test
    void testConvertInterval_whenIntervalIsNotCorrect_thenThrowException() {
        // Arrange
        String interval = "ERROR INTERVAL";
        // Act & Assert
        assertThrows(DataValidationException.class, () -> convertAnalyticsParam.convertInterval(interval));
    }

    @Test
    void testConvertFromDate_whenInputDateNotNull_thenConvertToDataTime() {
        // Act
        LocalDateTime result = convertAnalyticsParam.convertFromDate(date);
        // Assert
        assertEquals(result.toLocalDate(), date);
    }

    @Test
    void testConvertFromDate_whenInputDateNull_thenReturnNull() {
        // Act
        LocalDateTime result = convertAnalyticsParam.convertFromDate(dateNull);
        // Assert
        assertEquals(result, null);
    }

    @Test
    void testConvertToDate_whenInputDateNotNull_thenConvertToDataTime() {
        // Act
        LocalDateTime result = convertAnalyticsParam.convertToDate(date);
        // Assert
        assertEquals(result.toLocalDate(), date);
    }

    @Test
    void testConvertToDate_whenInputDateNull_thenReturnNull() {
        // Act
        LocalDateTime result = convertAnalyticsParam.convertToDate(dateNull);
        // Assert
        assertEquals(result, null);
    }
}