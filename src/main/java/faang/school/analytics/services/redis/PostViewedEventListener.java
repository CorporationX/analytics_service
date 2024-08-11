package faang.school.analytics.services.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostViewedEventListener implements MessageListener {
    public static List<String> messgeList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(message, PostViewEvent.class);
        messgeList.add(message.toString());
    }
}

