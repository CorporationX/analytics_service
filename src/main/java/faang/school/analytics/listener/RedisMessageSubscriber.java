package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.mapper.ProfileViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;
    @Autowired
    private ProfileViewEventMapper profileViewEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        body = body.replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}");

        ProfileViewEvent profileViewEvent;
        try {
            profileViewEvent = objectMapper.readValue(body, ProfileViewEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Received message from channel " + channel + ": " + profileViewEvent.toString());

        AnalyticsEvent analyticsEvent = profileViewEventMapper.toAnalyticsEvent(profileViewEvent);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        analyticsEventRepository.save(analyticsEvent);
    }
}
