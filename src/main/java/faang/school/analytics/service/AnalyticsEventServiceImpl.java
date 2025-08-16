package faang.school.analytics.service;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.request.GetAnalyticsRequestDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.repository.criteria.AnalyticsGetCriteria;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public AnalyticsEventDto saveEvent(@NonNull AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent event = analyticsEventMapper.toAnalyticsEvent(analyticsEventDto);
        analyticsEventRepository.save(event);
        return analyticsEventMapper.toAnalyticsEventDto(event);
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(@NonNull GetAnalyticsRequestDto getAnalyticsRequestDto) {
        return analyticsEventMapper.toAnalyticsEventDtoList(
                analyticsEventRepository.findByCriteria(getAnalyticsCriteriaFromRequest(getAnalyticsRequestDto))
        );
    }

    private AnalyticsGetCriteria getAnalyticsCriteriaFromRequest(GetAnalyticsRequestDto getAnalyticsRequestDto) {
        return AnalyticsGetCriteria.builder()
                .receiverId(getAnalyticsRequestDto.receiverId())
                .eventType(getAnalyticsRequestDto.eventType())
                .interval(getAnalyticsRequestDto.interval())
                .from(getAnalyticsRequestDto.from())
                .to(getAnalyticsRequestDto.to())
                .sortDirection(AnalyticsGetCriteria.SortDirection.DESC)
                .build();
    }
}
