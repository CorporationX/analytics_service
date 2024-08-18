package faang.school.analytics.config.converter;

import faang.school.analytics.service.Interval;
import org.springframework.core.convert.converter.Converter;

public class StringToIntervalConverter implements Converter<String, Interval> {
    @Override
    public Interval convert(String source) {
        return Interval.valueOf(source.toUpperCase());
    }
}
