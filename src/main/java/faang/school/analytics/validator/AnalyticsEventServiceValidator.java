package faang.school.analytics.validator;

import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventServiceValidator {

    public void validateMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
    }
}
