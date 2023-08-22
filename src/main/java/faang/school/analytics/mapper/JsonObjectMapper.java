package faang.school.analytics.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonObjectMapper {
    private final ObjectMapper objectMapper;

    public <T> T fromJson(byte[] json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            log.error("Error while converting json to object", e);
        }
        return null;
    }
}