package faang.school.analytics.service;

import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.repository.specification.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация интерфейса {@link AnalyticsEventService} для сохранения и получения ивентов
 *
 * @author Linempy
 * @since 20.08.2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository repository;

    @Override
    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        AnalyticsEvent saveEvent = repository.save(event);
        log.info("Ивент id={} был сохранен", saveEvent.getId());
    }

    @Override
    public List<AnalyticsEvent> getAnalytics(RecommendationFilterDto filterDto) {
        Specification<AnalyticsEvent> filter = SpecificationBuilder.buildSpecification(filterDto);
        return repository.findAll(filter);
    }
}