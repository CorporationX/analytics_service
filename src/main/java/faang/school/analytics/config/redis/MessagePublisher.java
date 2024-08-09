package faang.school.analytics.config.redis;

public interface MessagePublisher {
    void publish(String message);
}
