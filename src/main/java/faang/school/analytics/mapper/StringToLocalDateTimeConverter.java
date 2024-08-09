package faang.school.analytics.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    @Nullable
    public LocalDateTime convert(@Nullable String source) {
        if (source == null || source.trim().isEmpty()){
            return null;
        }

        try {
            return LocalDateTime.parse(source, DEFAULT_FORMATTER);
        }catch (DateTimeParseException e){
            throw new IllegalArgumentException("Invalid date-time format: " + source);
        }
    }
}
