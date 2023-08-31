package faang.school.analytics.service;

import faang.school.analytics.dto.redis.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    public List<AnalyticsEventDto> getAnalytcs(Long id, String eventType, String intervalType, String period) {
        EventType eventTypeFromRequest = EventType.valueOf(eventType);
        List<AnalyticsEvent> result = new ArrayList<>();
        Stream<AnalyticsEvent> analyticEvents = analyticsEventRepository.findByReceiverIdAndEventType(id, eventTypeFromRequest);
        if(intervalType != null && period != null) {
            result = analyticEvents
                    .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                    .map(a)


        }

        return result;
    }
}
