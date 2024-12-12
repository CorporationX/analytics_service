package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FundRaisedEvent;
import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.mapper.event.EventMapper;
import faang.school.analytics.mapper.fund_raised.FundRaisedEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.event.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FundRaisedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final FundRaisedEventMapper fundRaisedEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FundRaisedEvent fundRaisedEvent = objectMapper.readValue(message.getBody(), FundRaisedEvent.class);
            EventDto eventDto = fundRaisedEventMapper.toEventDto(fundRaisedEvent);
            analyticsEventService.addNewEvent(eventDto);
        } catch (IOException e) {
            log.error("Error processing FundRaisedEvent", e);
            throw new RuntimeException(e);
        }
    }
}
