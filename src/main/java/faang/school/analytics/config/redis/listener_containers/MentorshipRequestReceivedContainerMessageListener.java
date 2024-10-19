package faang.school.analytics.config.redis.listener_containers;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestReceivedContainerMessageListener implements RedisContainerMessageListener {
    private final MessageListenerAdapter adapter;
    private final Topic topic;

    public MentorshipRequestReceivedContainerMessageListener(MessageListener mentorshipRequestReceivedEventListener,
                                                             Topic mentorshipRequestReceivedTopicName) {
        this.adapter = new MessageListenerAdapter(mentorshipRequestReceivedEventListener);
        this.topic = mentorshipRequestReceivedTopicName;
    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Topic getTopic() {
        return topic;
    }
}
