package faang.school.analytics.service;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.dto.RequestAnalyticsDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(EventDto eventDto) {
        analyticsEventRepository.save(analyticsEventMapper.toEntity(eventDto));
    }

    public List<EventDto> getAnalitics(RequestAnalyticsDto requestAnalyticsDto) {

        Stream<AnalyticsEvent> event = analyticsEventRepository.findByReceiverIdAndEventType(
                requestAnalyticsDto.getReceiverId(), requestAnalyticsDto.getEventType()
        );
        if(requestAnalyticsDto.getInterval() != null) {
            Pair<LocalDateTime, LocalDateTime> actualInterval = requestAnalyticsDto.getInterval().getInterval();
            return getFilteredAnalytics(event, actualInterval.getFirst(), actualInterval.getSecond());
        }
        else if(requestAnalyticsDto.getFrom() != null && requestAnalyticsDto.getTo() != null) {
            return getFilteredAnalytics(event, requestAnalyticsDto.getFrom(), requestAnalyticsDto.getTo());
        }
        else {
            throw new IllegalArgumentException("Invalid interval");
        }
    }

    private List<EventDto> getFilteredAnalytics(Stream<AnalyticsEvent> event, LocalDateTime from, LocalDateTime to) {
        return event.filter(e -> e.getReceivedAt().isAfter(from) && e.getReceivedAt()
                        .isBefore(to))
                .sorted((a, b) -> b.getReceivedAt().compareTo(a.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}
