package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService<T> {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper<T> analyticsEventMapper;

    public void saveEvent(T eventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(eventDto);
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional
    public Stream<AnalyticsEvent> getAnalytics(long receiverId, EventType type) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, type);
    }
}
