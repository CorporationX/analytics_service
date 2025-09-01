package faang.school.analytics.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Класс с константами и прочими данными для интеграционных тестов слушателей
 *
 * @author Linempy
 * @since 02.09.2025
 */
@Component
public class MessageListenerTestData {
    @Value("${redis.topic.recommendation}")
    String recommendationTopic;
}