package faang.school.analytics.service;

import faang.school.analytics.dto.message.AnalyticsEventDto;
import faang.school.analytics.mapper.MentorshipRequestMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final TimeIntervalsService timeIntervalsService;
    private final MentorshipRequestMapper mapper;

    public void SaveAnalyticsEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, String eventType,
                                                String interval, String startDate, String endDate) {

        EventType type = EventType.valueOf(eventType);

        if (interval != null) {
            Map<String, LocalDateTime> dateFromInterval = timeIntervalsService.getInterval(interval);

            LocalDateTime startInterval = dateFromInterval.get(TimeIntervalsService.START_INTERVAL);
            LocalDateTime endInterval = dateFromInterval.get(TimeIntervalsService.END_INTERVAL);

            return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, type)
                    .filter(event -> event.getReceivedAt().isAfter(startInterval) && event.getReceivedAt().isBefore(endInterval))
                    .map(mapper::toAnalyticsEventDto)
                    .toList();
        } else {
            LocalDateTime convertStartDate = LocalDateTime.parse(startDate);
            LocalDateTime convertEndDate = LocalDateTime.parse(endDate);

            return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, type)
                    .filter(event -> event.getReceivedAt().isAfter(convertStartDate) && event.getReceivedAt().isBefore(convertEndDate))
                    .map(mapper::toAnalyticsEventDto)
                    .toList();
        }
    }
}
