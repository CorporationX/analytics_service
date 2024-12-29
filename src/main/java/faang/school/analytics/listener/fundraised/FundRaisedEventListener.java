package faang.school.analytics.listener.fundraised;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.fundraised.FundRaisedEvent;
import faang.school.analytics.mapper.fundraised.FundRaisedMapper;
import faang.school.analytics.service.events.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FundRaisedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final FundRaisedMapper fundRaisedMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FundRaisedEvent fundRaisedEvent = objectMapper.readValue(message.getBody(), FundRaisedEvent.class);
            AnalyticsEventDto analyticsEventDto = fundRaisedMapper.fundRaisedtoAnalyticsEventDto(fundRaisedEvent);
            log.info("received the message: {}, send it to the service", fundRaisedEvent);
            analyticsEventService.saveFundRaisedEvent(analyticsEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
