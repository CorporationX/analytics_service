package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.mentorship.MentorshipRequestEvent;
import faang.school.analytics.exception.DataTransformationException;
import faang.school.analytics.exception.ExceptionMessages;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipRequestedEventListener implements MessageListener {
//    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message: {}", message.getBody());
        try {
            MentorshipRequestEvent mentorshipRequestEvent = objectMapper.readValue(message.getBody(), MentorshipRequestEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(mentorshipRequestEvent);
            analyticsEvent.setEventType(EventType.MENTORSHIP_RECEIVED);
//            analyticsEventService.save(analyticsEvent);
        } catch (Exception e) {
            log.error(ExceptionMessages.INVALID_TRANSFORMATION, e);
            throw new DataTransformationException(ExceptionMessages.INVALID_TRANSFORMATION, e);
        }
    }
}