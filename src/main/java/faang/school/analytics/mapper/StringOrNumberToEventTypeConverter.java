package faang.school.analytics.mapper;

import faang.school.analytics.model.EventType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringOrNumberToEventTypeConverter implements Converter<String, EventType> {
    @Override
    public EventType convert(@NonNull String source) {
        try {
            int number = Integer.parseInt(source);
             return EventType.of(number);
        }catch (NumberFormatException e){
            return EventType.valueOf(source);
        }
    }
}
