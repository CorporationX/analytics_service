package faang.school.analytics.service;

import faang.school.analytics.dto.fundRasing.FundRaisedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FundRaisedEventWorker {

    private final AnalyticsEventService service;
    private final AnalyticsEventMapper mapper;

    public void save(FundRaisedEvent event) {
        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
        service.save(analyticsEvent);
    }
}
