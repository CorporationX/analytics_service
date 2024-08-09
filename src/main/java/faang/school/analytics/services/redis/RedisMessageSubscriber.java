package faang.school.analytics.services.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisMessageSubscriber {
    public static List<String> messgeList = new ArrayList<>();

    public void onMessage(Message message, byte[] pattern) {
        messgeList.add(message.toString());
    }
}

