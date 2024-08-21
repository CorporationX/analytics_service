package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static faang.school.analytics.model.Interval.getDaysByInterval;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticEventDto> getAnalytics(AnalyticInfoDto analyticInfoDto) {

        long receiverId = analyticInfoDto.getReceiverId();
        EventType eventType = analyticInfoDto.getEventType();
        Interval interval = analyticInfoDto.getInterval();
        LocalDateTime from = analyticInfoDto.getFrom();
        LocalDateTime to = analyticInfoDto.getTo();

        List<AnalyticsEvent> analyticsEvents = interval == null ?
                analyticsEventRepository.getBetweenDate(from, to) :
                analyticsEventRepository.getByDays(getCurrentDateTimeMinusIntervalDays(interval));

        return analyticsEvents.stream()
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private LocalDateTime getCurrentDateTimeMinusIntervalDays(Interval interval) {
        int daysByInterval = getDaysByInterval(interval);
        return LocalDateTime.now().minusDays(daysByInterval);
    }
}
