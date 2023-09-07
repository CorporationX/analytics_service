package faang.school.analytics.service;

import faang.school.analytics.dto.fundRasing.FundRaisedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FundRaisedEventHandler {

    private final AnalyticsEventService service;
    private final AnalyticsEventMapper mapper;

    public void save(FundRaisedEvent event) {
        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
        service.save(analyticsEvent);
        log.info("Saved fund raised event: {}", event);
    }
}
