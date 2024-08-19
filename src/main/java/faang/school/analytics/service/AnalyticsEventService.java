package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.LikeEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.param.AnalyticsRequestParams;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import faang.school.analytics.validator.AnalyticsEventServiceValidator;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventValidator analyticsEventValidator;
    private final LikeEventMapper likeEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        analyticsEventValidator.validateAnalyticsEvent(event);
        return analyticsEventMapper.toDto(analyticsEventRepository.save(event));
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, String eventType, Optional<String> interval, Optional<String> startDate, Optional<String> endDate) {
        AnalyticsRequestParams params = new AnalyticsRequestParams(receiverId, eventType, interval, startDate, endDate);
        analyticsEventValidator.validateParams(receiverId, params.getEventType(), params.getInterval(), params.getStartDate(), params.getEndDate());
        Stream<AnalyticsEvent> result = analyticsEventRepository.findByReceiverIdAndEventType(params.getReceiverId(), params.getEventType());
        if (params.getInterval() != null) {
            params.setStartDate(LocalDateTime.now().minus(params.getInterval().getTemporalAmount(params.getInterval())));
            params.setEndDate(LocalDateTime.now());
        }
        LocalDateTime finalFrom = params.getStartDate();
        LocalDateTime finalTo = params.getEndDate();
        return result.filter(event -> event.getReceivedAt().isAfter(finalFrom) && event.getReceivedAt().isBefore(finalTo))
                .sorted((item1, item2) -> item2.getReceivedAt().compareTo(item1.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }

    public void saveLikeEvent(@NotNull LikeEvent likeEvent) {
        AnalyticsEvent analyticsEvent = likeEventMapper.toEntity(likeEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}