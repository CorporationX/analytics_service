package faang.school.analytics.service.redis.listeners;

import faang.school.analytics.mapper.UserEventMapper;
import faang.school.analytics.service.redis.events.UserEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventsListener implements MessageListener {
    @Value("${spring.data.redis.channels.user_events_channel.name}")
    private String defaultChannelName;
    private List<String> subscribedChannels = new ArrayList<>();

    private UserEventMapper userEventMapper;

    @PostConstruct
    private void postConstruct() {
        subscribedChannels.add(defaultChannelName);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        UserEvent userEvent = userEventMapper.readValue(message.getBody(), UserEvent.class);

        // Depends on channel name we can write here different logic
        log.info("Received message: " + body + " from channel: " + channel);
        System.out.println("Received message: " + body + " from channel: " + channel);
    }
}
