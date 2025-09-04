package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long>,
        JpaSpecificationExecutor<AnalyticsEvent> {

    default AnalyticsEvent findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Аналитика события id=%d не была найдена".formatted(id))
                );
    }
}
