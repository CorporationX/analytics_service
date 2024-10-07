package faang.school.analytics.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class CommentEventListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
