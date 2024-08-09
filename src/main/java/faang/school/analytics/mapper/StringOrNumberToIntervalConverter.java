package faang.school.analytics.mapper;

import faang.school.analytics.util.Interval;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringOrNumberToIntervalConverter implements Converter<String, Interval> {
    @Override
    @Nullable
    public Interval convert(@Nullable String source) {
        if (source == null) {
            return null;
        }

        try{
            int number = Integer.parseInt(source);
            return Interval.fromStringOrNumber(number);
        }catch (NumberFormatException e){
            return Interval.fromStringOrNumber(source);
        }
    }
}
