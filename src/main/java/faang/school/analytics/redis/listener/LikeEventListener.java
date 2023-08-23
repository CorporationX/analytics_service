package faang.school.analytics.redis.listener;

import faang.school.analytics.service.LikeEventService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class LikeEventListener implements MessageListener {
    public static List<String> messageList = new ArrayList<>();

    private final LikeEventService likeEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received : " + message.toString());
        likeEventService.saveMessage(message.toString());
    }
}