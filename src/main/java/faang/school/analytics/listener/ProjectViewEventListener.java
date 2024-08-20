package faang.school.analytics.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class ProjectViewEventListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
