package faang.school.analytics.config.context;

import faang.school.analytics.mapper.StringOrNumberToEventTypeConverter;
import faang.school.analytics.mapper.StringOrNumberToIntervalConverter;
import faang.school.analytics.mapper.StringToLocalDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final StringOrNumberToEventTypeConverter stringOrNumberToEventTypeConverter;
    private final StringOrNumberToIntervalConverter stringOrNumberToIntervalConverter;
    private final StringToLocalDateTimeConverter stringToLocalDateTimeConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringOrNumberToEventTypeConverter);
        registry.addConverter(stringOrNumberToIntervalConverter);
        registry.addConverter(stringToLocalDateTimeConverter);
    }
}
