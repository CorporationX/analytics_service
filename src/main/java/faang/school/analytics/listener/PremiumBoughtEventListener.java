package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.PremiumBoughtEvent;

import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class PremiumBoughtEventListener  implements MessageListener{

        private final AnalyticsEventService analyticsEventService;
        private final ObjectMapper objectMapper;

        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                log.info("Received search appearance message");
                PremiumBoughtEvent event = objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class);
                analyticsEventService.processPremiumBoughtEvent(event);
                AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();
                analyticsEventDto.setId(event.getUserId());
                analyticsEventDto.setEventType(EventType.PREMIUM_SUBSCRIPTION);
                analyticsEventDto.setReceivedAt(LocalDateTime.now());
                analyticsEventDto.setActorId(event.getUserId());
                analyticsEventDto.setReceiverId(event.getUserId());
                analyticsEventService.saveEvent(analyticsEventDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
