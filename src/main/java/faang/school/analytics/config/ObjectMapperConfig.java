package faang.school.analytics.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ObjectMapperConfig {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    @Bean("objectMapperAnalysisComments")
    public ObjectMapper objectMapperAnalysisComments() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        );

        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }
}