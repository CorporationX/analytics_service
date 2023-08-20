package faang.school.analytics.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class Mapper {
    private ObjectMapper objectMapper;

    public <T> T toObject(String json, Class<T> valueType) {
        T object;
        try {
            object = objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            log.error("Exception with json mapping: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return object;
    }
}
