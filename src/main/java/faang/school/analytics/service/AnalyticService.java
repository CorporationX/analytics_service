package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticRequestDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.DateRange;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public List<AnalyticsEvent> getAnalytics(AnalyticRequestDto analyticRequestDto) {
        AnalyticRequestDto checkDto = ensureStartAndEndAreSet(analyticRequestDto);
        return analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(checkDto.getReceiverId(), checkDto.getEventType(), checkDto.getStartDate(), checkDto.getEndDate()).collect(Collectors.toList());
    }

    private AnalyticRequestDto ensureStartAndEndAreSet(AnalyticRequestDto analyticRequestDto){
        if (analyticRequestDto.getStartDate() == null || analyticRequestDto.getEndDate() == null) {
            DateRange range = Interval.getDateRange(analyticRequestDto.getInterval());
            analyticRequestDto.setStartDate(range.getStartDate());
            analyticRequestDto.setEndDate(range.getEndDate());
        }
        return analyticRequestDto;
    }
}