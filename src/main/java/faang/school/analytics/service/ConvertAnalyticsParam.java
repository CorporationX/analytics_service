package faang.school.analytics.service;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.Interval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

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

    public Optional<LocalDateTime> convertFromDate(LocalDate date) {
        return Optional.ofNullable(date).map(id -> LocalDateTime.of(id, LocalTime.MIN));
    }

    public Optional<LocalDateTime> convertToDate(LocalDate date) {
        return Optional.ofNullable(date).map(id -> LocalDateTime.of(id, LocalTime.MAX));
    }
}