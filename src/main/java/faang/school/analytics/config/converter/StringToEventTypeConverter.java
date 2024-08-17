package faang.school.analytics.config.converter;

import faang.school.analytics.model.EventType;
import org.springframework.core.convert.converter.Converter;

public class StringToEventTypeConverter implements Converter<String, EventType> {
    @Override
    public EventType convert(String source) {
        return EventType.valueOf(source.toUpperCase());
    }
}
