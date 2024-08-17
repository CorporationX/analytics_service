package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.param.AnalyticsRequestParams;
import faang.school.analytics.model.LikeEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import faang.school.analytics.validator.AnalyticsEventServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AnalyticsEventServiceValidator analyticsEventServiceValidator;

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

    public void saveLikeEvent(Message message) {
        analyticsEventServiceValidator.validateMessage(message);

        String[] data = message.toString().split(",");

        LikeEvent likeEvent = new LikeEvent();
        likeEvent.setPostId(Long.parseLong(data[0]));
        likeEvent.setAuthorId(Long.parseLong(data[1]));
        likeEvent.setUserId(Long.parseLong(data[2]));
        likeEvent.setTimestamp(LocalDateTime.parse(data[3]));

        AnalyticsEvent analyticsEvent = analyticsEventMapper.likeEventToAnalyticsEvent(likeEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}
