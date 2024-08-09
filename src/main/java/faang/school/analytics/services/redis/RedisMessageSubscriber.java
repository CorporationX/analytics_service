package faang.school.analytics.services.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisMessageSubscriber implements MessageListener {
    public static List<String> messgeList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        messgeList.add(message.toString());
    }
}

