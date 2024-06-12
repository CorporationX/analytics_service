package faang.school.analytics.util.converter;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.interval.Interval;
import faang.school.analytics.model.interval.TypeOfInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class AnalyticsParametersConverter {
    public Interval convertInterval(String title, Long quantity) {
        return Optional.ofNullable(title).map(name -> {
            Interval result = new Interval();
            try {
                result.setTypeOfInterval(TypeOfInterval.valueOf(title));
            } catch (IllegalArgumentException e) {
                log.error("параметр TypeOfInterval введен неверно", e);
                throw new DataValidationException(String.format("указано неверное значение для параметра TypeOfInterval: %s." +
                        "Для корректной работы программы укажите один из доступных вариантов: %s", title, Arrays.toString(TypeOfInterval.values())));
            }
            if (quantity == null) {
                log.error("параметр quantity для указания количества единиц интервала не указан");
                throw new DataValidationException("укажите количество единиц интервала для корректной работы программы");
            }
            result.setQuantity(quantity);
            return result;
        }).orElse(null);
    }

    public Optional<LocalDateTime> convertDateTimeFrom(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(date -> LocalDateTime.of(date, LocalTime.MIN));
    }

    public Optional<LocalDateTime> convertDateTimeTo(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(date -> LocalDateTime.of(date, LocalTime.MAX));
    }
}