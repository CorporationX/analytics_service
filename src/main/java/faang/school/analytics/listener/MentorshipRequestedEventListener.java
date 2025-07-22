package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MentorshipRequestedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipEventDto eventDto = objectMapper.readValue(message.getBody(), MentorshipEventDto.class);
            analyticsService.processMentorshipEvent(eventDto);
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения Redis", e);
        }
    }
}