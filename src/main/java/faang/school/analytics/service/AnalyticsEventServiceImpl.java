package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.EventSavingFailureException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        isExistingEvent(event);
        return analyticsEventRepository.save(event);
    }

    @Override
    @Transactional
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType,
                                                Interval interval, LocalDateTime from, LocalDateTime to) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> isWithinDateRange(event, interval, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();
    }

    private boolean isWithinDateRange(AnalyticsEvent event, Interval interval,
                                      LocalDateTime from, LocalDateTime to) {
        LocalDateTime dateReceived = event.getReceivedAt();
        if (interval != null) {
            return LocalDateTime.now()
                    .minus(interval.getDays(), interval.getUnit())
                    .isBefore(dateReceived);
        } else {
            return (dateReceived.isAfter(from) && dateReceived.isBefore(to)
                    && !dateReceived.isAfter(LocalDateTime.now()));
        }
    }

    private void isExistingEvent(AnalyticsEvent event) {
        if (analyticsEventRepository.existsById(event.getId())) {
            throw new EventSavingFailureException("Save operation not permitted: event already exists");
        }
    }
}
