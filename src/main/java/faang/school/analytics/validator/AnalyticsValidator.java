package faang.school.analytics.validator;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsValidator {
    public void validateInterval(Interval interval, LocalDateTime from, LocalDateTime to){
        if (interval == null) {
            if (from == null || to == null) {
                throw new DataValidationException
                        ("Параметры from и to не должны быть равны null, если не задан интервал");
            }
        }
    }

    public void validateEventType(EventType eventType){
        if (eventType == null) {
            throw new DataValidationException("Тип события не может равняться null");
        }
    }
}
