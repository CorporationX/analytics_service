package faang.school.analytics.service;

import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AnalyticsValidationService {

    public void validateAnalyticsRequest(Optional<Interval> interval, LocalDateTime from, LocalDateTime to) {
        if (interval.isEmpty() && (from == null || to == null)) {
            throw new IllegalArgumentException("Необходимо указать либо интервал, либо обе даты (from и to)");
        }

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Дата начала не может быть позже даты окончания");
        }
    }
}