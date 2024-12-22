package faang.school.analytics.service;

import faang.school.analytics.dto.PremiumBoughtEvent;

import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperToLog;
import faang.school.analytics.mappers.AnalyticsEventMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.AnalyticsFilterI;
import faang.school.analytics.filter.Interval;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<AnalyticsFilterI> analyticsFilters;
    private final AnalyticsEventMapperToLog analyticsEventMapperToLog;

    public void processEvent(SearchAppearanceEvent event) {
        String logEntry = analyticsEventMapperToLog.mapToLog(event);
        log.info("Processing event: " + logEntry);
    }

    public void processPremiumBoughtEvent(PremiumBoughtEvent event) {
        String logEntry = analyticsEventMapperToLog.mapToLog(event);
        log.info("Processing event: " + logEntry);
    }

    public void processRecommendationEvent(RecommendationEvent event) {
        AnalyticsEventDto dto = convertToDto(event);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(dto);
        analyticsEventRepository.save(analyticsEvent);
    }

    private AnalyticsEventDto convertToDto(RecommendationEvent event) {
        return AnalyticsEventDto.builder()
                .id(event.getRecommendationId())
                .actorId(event.getAuthorId())
                .receiverId(event.getReceiverId())
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(event.getCreatedAt())
                .build();
    }

    public void saveEvent(AnalyticsEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional
    public List<AnalyticsEventDto> getAnalytics(AnalyticsFilterDto analyticsFilterDto) {

        if (analyticsFilterDto.getInterval() != null && (analyticsFilterDto.getTo() != null && analyticsFilterDto.getFrom() != null)) {
            throw new IllegalArgumentException("You can't use both interval and from/to");
        }

        Stream<AnalyticsEvent> eventsStream = analyticsEventRepository.findByReceiverIdAndEventType(
                analyticsFilterDto.getReceiverId(), analyticsFilterDto.getEventType());

        try (eventsStream) {
            Stream<AnalyticsEvent> filteredStream = eventsStream;

            for (AnalyticsFilterI filter : analyticsFilters) {
                if (filter.isApplicable(analyticsFilterDto)) {
                    filteredStream = filter.apply(filteredStream, analyticsFilterDto);
                }
            }

            Interval interval = analyticsFilterDto.getInterval();
            if (interval != null) {
                filteredStream = filteredStream.filter(event -> interval.contains(event.getReceivedAt()));
            }

            return filteredStream
                    .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                    .map(analyticsEventMapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}