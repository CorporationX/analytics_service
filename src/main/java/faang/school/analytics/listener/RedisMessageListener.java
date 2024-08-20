package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class RedisMessageListener<T> {
    private ObjectMapper objectMapper;

}
