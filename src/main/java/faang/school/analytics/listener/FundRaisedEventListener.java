package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FundRaisedEvent;
import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.mapper.fund_raised.FundRaisedEventMapper;
import faang.school.analytics.service.event.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FundRaisedEventListener extends AbstractListenerForAddEvent<FundRaisedEvent>{
    private final AnalyticsEventService analyticsEventService;
    private final FundRaisedEventMapper fundRaisedEventMapper;

    public FundRaisedEventListener(ObjectMapper objectMapper,
                                   AnalyticsEventService analyticsEventService,
                                   FundRaisedEventMapper fundRaisedEventMapper) {
        super(objectMapper);
        this.analyticsEventService = analyticsEventService;
        this.fundRaisedEventMapper = fundRaisedEventMapper;
    }

    @Override
    public Class<FundRaisedEvent> getEventClass() {
        return FundRaisedEvent.class;
    }

    @Override
    public void handleEvent(FundRaisedEvent event) {
        EventDto eventDto = fundRaisedEventMapper.toEventDto(event);
        analyticsEventService.addNewEvent(eventDto);
    }
}
