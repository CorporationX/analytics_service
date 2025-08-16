package faang.school.analytics.listener;

import faang.school.analytics.analytics_event.MentorshipRequestedEvents;
import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.service.MentorshipEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
@RequiredArgsConstructor
public class MentorshipRequestedEventListener implements MessageListener {

    private final MentorshipEventService mentorshipEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            Object body = message.getBody();
            if (body instanceof MentorshipRequestedEvents event) {
                log.info("Получено событие: {}", event);

                var dto = new MentorshipEventDto(
                        event.getSenderId(),
                        event.getReceiverId(),
                        LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(event.getTimestamp()), ZoneOffset.UTC)
                );

                mentorshipEventService.saveEvent(dto);
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке события", e);
        }
    }
}