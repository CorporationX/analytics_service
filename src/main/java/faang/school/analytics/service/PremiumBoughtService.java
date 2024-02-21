package faang.school.analytics.service;

import faang.school.analytics.dto.PremiumBoughtEventDto;
import faang.school.analytics.mapper.PremiumBoughtMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PremiumBoughtService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final PremiumBoughtMapper premiumBoughtMapper;

    public void saveEvent(PremiumBoughtEventDto eventDto) {
        AnalyticsEvent analyticsEvent = premiumBoughtMapper.toAnalyticsEvent(eventDto);
        analyticsEvent.setEventType(EventType.PREMIUM_BOUGHT);
        analyticsEventRepository.save(analyticsEvent);
    }

    public Stream<AnalyticsEvent> getAnalytics(long id, EventType type) {
        return analyticsEventRepository.findByReceiverIdAndEventType(id, type);
    }
}
