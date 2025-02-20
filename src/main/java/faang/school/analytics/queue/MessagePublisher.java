package faang.school.analytics.queue;

public interface MessagePublisher {

    void publish(final String message);
}