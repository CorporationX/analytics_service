package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticRequestDto;
import faang.school.analytics.dto.AnalyticResponseDto;
import faang.school.analytics.mapper.AnalyticMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validation.AnalyticValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticMapper mapper;
    private final AnalyticValidation analyticValidation;

    @Transactional
    public List<AnalyticResponseDto> getAnalytics(AnalyticRequestDto analyticRequestDto) {
        AnalyticRequestDto checkDto = analyticValidation.ensureStartAndEndAreSet(analyticRequestDto);
        List<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(checkDto.getReceiverId(), checkDto.getEventType(), checkDto.getStartDate(), checkDto.getEndDate()).collect(Collectors.toList());
        return mapper.toDtoList(events);
    }
}