package faang.school.analytics.listner;

import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.listner.abstract_listener.AbstractEventListener;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectViewListener extends AbstractEventListener<ProjectViewEvent> implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        saveAnalyticsEvent(message, ProjectViewEvent.class);
    }
}