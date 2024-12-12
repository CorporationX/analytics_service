package faang.school.analytics.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
@Slf4j
@Component
public class PremiumEventProcessor {

    public void processEvent(Map<String, Object> event) {
        log.info("Processing event: {}", event);
        // Выполните действия с данными события
    }
}