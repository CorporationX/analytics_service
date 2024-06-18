package faang.school.analytics.util.converter;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.interval.Interval;
import faang.school.analytics.model.interval.TypeOfInterval;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class AnalyticsParametersConverterTest {
    private final AnalyticsParametersConverter converter = new AnalyticsParametersConverter();
    private final LocalDate date = LocalDate.now();

    @Test
    public void whenConvertIntervalAndAllIsCorrectThenGetInterval() {
        String typeOfInterval = "HOURS";
        long quantity = 2;
        Interval actual = converter.convertInterval(typeOfInterval, quantity);
        assertThat(actual.getTypeOfInterval()).isEqualTo(TypeOfInterval.HOURS);
    }

    @Test
    public void whenConvertIntervalAndTitleIsNullThenReturnNull() {
        Interval actual = converter.convertInterval(null, null);
        assertThat(actual).isNull();
    }

    @Test
    public void whenConvertIntervalAndQuantityIsNullThenReturnNull() {
        String typeOfInterval = "HOURS";
        Assert.assertThrows(DataValidationException.class,
                () -> converter.convertInterval(typeOfInterval, null));
    }

    @Test
    public void whenConvertFromDateAndInputDateNotNullThenConvertToDataTime() {
        LocalDateTime result = converter.convertDateTimeFrom(date).get();
        assertThat(result.toLocalDate()).isEqualTo(date);
    }

    @Test
    public void whenConvertFromDateAndInputDateNullThenGetNull() {
        Optional<LocalDateTime> result = converter.convertDateTimeFrom(null);
        assertThat(result).isEmpty();
    }

    @Test
    public void whenConvertToDateAndInputDateNotNullThenConvertToDataTime() {
        LocalDateTime result = converter.convertDateTimeTo(date).get();
        assertThat(result.toLocalDate()).isEqualTo(date);
    }

    @Test
    public void whenConvertToDateAndInputDateNullThenGetNull() {
        Optional<LocalDateTime> result = converter.convertDateTimeTo(null);
        assertThat(result).isEmpty();
    }
}