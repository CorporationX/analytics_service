package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.model.Interval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void saveEvent(AnalyticsEvent event) {
        analyticsRepository.save(event);
        log.info("event has been saved to DB," +
                        " event type = {}, receiver = {}, actor = {}, event id = {}, received at = {}",
                event.getEventType(), event.getReceiverId(), event.getActorId(), event.getId(), event.getReceivedAt());
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(
            long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        AnalyticsInterval analyticsInterval = setAnalyticsInterval(interval, from, to);
        List<AnalyticsEvent> analyticsEventList = analyticsRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event ->
                        event.getReceivedAt().isAfter(analyticsInterval.startDate)
                                && event.getReceivedAt().isBefore(analyticsInterval.endDate))
                .toList();

        return analyticsEventList.stream()
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private record AnalyticsInterval(
            LocalDateTime startDate,
            LocalDateTime endDate
    ){}


    private AnalyticsInterval setAnalyticsInterval(Interval interval, LocalDateTime from, LocalDateTime to){
        LocalDateTime startDate;
        LocalDateTime endDate;
        if (interval != null) {
            endDate = LocalDateTime.now();
            startDate = switch (interval) {
                case DAY -> endDate.minusDays(1);
                case WEEK -> endDate.minusWeeks(1);
                case MONTH -> endDate.minusMonths(1);
            };
        } else {
            startDate = from;
            endDate = to;
        }
        return new AnalyticsInterval(startDate, endDate);
    }

}
