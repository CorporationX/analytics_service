package faang.school.analytics.service;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.Interval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@Slf4j
@Component
public class ConvertAnalyticsParam {

    public Interval convertInterval(String interval) {

        if (interval != null) {
            try {
                return Interval.valueOf(interval);
            } catch (IllegalArgumentException e) {
                log.error("Ошибка ввода параметра", e);
                throw new DataValidationException("Введено не корректное значение для параметра interval :"
                        + interval + "\nДоступные варианты: " + Arrays.toString(Interval.values()));
            }
        } else return null;
    }

    public LocalDateTime convertFromDate(LocalDate date) {
        if (date != null) {
            return LocalDateTime.of(date, LocalTime.MIN);
        } else return null;
    }

    public LocalDateTime convertToDate(LocalDate date) {

        if (date != null) {
            return LocalDateTime.of(date, LocalTime.MAX);
        } else return null;
    }
}