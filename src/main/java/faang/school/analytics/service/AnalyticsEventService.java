package faang.school.analytics.service;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.dto.event.SortField;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.repository.filter.AnalyticsFilterRepository;
import faang.school.analytics.validator.analyticsEvent.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventValidator analyticsEventValidator;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserContext userContext;
    private final List<AnalyticsFilterRepository> analyticsFilterRepositories;

    @Transactional
    public Page<AnalyticsEventDto> getAnalytics(AnalyticsFilterDto analyticsFilter) {
        LocalDateTime startTime = analyticsFilter.getFrom();
        LocalDateTime endTime = analyticsFilter.getTo();
        analyticsEventValidator.allDateIntervalsAreEmpty(analyticsFilter.getInterval(), startTime, endTime);

        if (analyticsFilter.getInterval() == null) {
            analyticsEventValidator.customIntervalIsValid(startTime, endTime);
        }

        Optional<Specification<AnalyticsEvent>> analyticsSpecification = analyticsFilterRepositories.stream()
                .filter(filter -> filter.isApplicable(analyticsFilter))
                .map(filter -> filter.apply(analyticsFilter))
                .reduce(Specification::and);

        analyticsSpecification.orElseThrow(() -> new DataValidationException("Required fields are incorrect"));

        Pageable pageRequest = preparePageRequest(
                analyticsFilter.getPage(),
                analyticsFilter.getSize(),
                analyticsFilter.getSortField(),
                analyticsFilter.getDirection());

        return  analyticsEventRepository.findAll(analyticsSpecification.get(), pageRequest).map(analyticsEventMapper::toDto);
    }

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEvent) {
        AnalyticsEvent analyticsToSave = analyticsEventMapper.toEntity(analyticsEvent);

        return analyticsEventMapper.toDto(analyticsEventRepository.save(analyticsToSave));
    }

    public AnalyticsEventDto prepareAnalyticsToSave(AnalyticsEventDto analyticsEvent) {
        analyticsEvent.setActorId(userContext.getUserId());
        analyticsEvent.setActorId(analyticsEvent.getActorId() == null
                ? userContext.getUserId()
                : analyticsEvent.getActorId());
        analyticsEvent.setReceivedAt(analyticsEvent.getReceivedAt() == null
                ? LocalDateTime.now()
                : analyticsEvent.getReceivedAt());

        return analyticsEvent;
    }

    public Pageable preparePageRequest(int page, int size, SortField sortField, Sort.Direction direction) {
        Sort sort = Sort.by(SortField.valueOf(sortField.name()).getValue());
        sort = (direction.isAscending()) ? sort.ascending() : sort.descending();
        return PageRequest.of(page, size, sort);
    }
}
