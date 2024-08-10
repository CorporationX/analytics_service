package faang.school.analytics.config;

import faang.school.analytics.config.converter.StringToEventTypeConverter;
import faang.school.analytics.config.converter.StringToIntervalConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEventTypeConverter());
        registry.addConverter(new StringToIntervalConverter());
    }
}
