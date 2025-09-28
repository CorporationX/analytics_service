package faang.school.analytics.messagelistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.messagelistener.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.mentorshipevent.MentorshipRequestedEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestedEvent mentorshipRequestedEvent = objectMapper.readValue(message.getBody(),
                MentorshipRequestedEvent.class);
        //analyticsEventService.save(analyticsEventMapper.toAnalyticsEvent(mentorshipRequestedEvent));
        analyticsEventService.save();
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("Received message from channel " + channel + " : " + body);
        log.info(mentorshipRequestedEvent.menteeId() + " " +  mentorshipRequestedEvent.mentorId()+ " "
                + " " + mentorshipRequestedEvent.requestTime());

    }


}
