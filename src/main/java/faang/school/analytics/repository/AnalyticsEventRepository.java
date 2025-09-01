package faang.school.analytics.repository;

import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.specification.SpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long>,
        JpaSpecificationExecutor<AnalyticsEvent> {

    default List<AnalyticsEvent> findByFilter(RecommendationFilterDto filterDto) {
        Specification<AnalyticsEvent> filter = SpecificationBuilder.buildSpecification(filterDto);
        return findAll(filter);
    }

    default AnalyticsEvent findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Аналитика события id=%d не была найдена".formatted(id))
                );
    }
}
