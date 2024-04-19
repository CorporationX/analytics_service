package faang.school.analytics.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonMapper {
    private final ObjectMapper mapper;

    public <T> String toJson(T event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Object mapping of a list is not successful", e);
            throw new RuntimeException(e);
        }
    }

    public <T> T toEvent(byte[] byteCode, Class<T> eventClass) {
        try {
            return mapper.readValue(byteCode, eventClass);
        } catch (IOException e) {
            log.error("Deserialization failed", e);
            throw new RuntimeException(e);
        }
    }
}
