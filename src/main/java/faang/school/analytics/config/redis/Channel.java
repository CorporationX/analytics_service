package faang.school.analytics.config.redis;

import lombok.Data;

/**
 * Класс для RedisProperties для заполнения названия топиков из внешних переменных для Redis
 */
@Data
public class Channel {
    private String recommendationChannel;
    private String subscriptionChannel;
}
