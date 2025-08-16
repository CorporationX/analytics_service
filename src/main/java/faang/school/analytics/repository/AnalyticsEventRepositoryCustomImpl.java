package faang.school.analytics.repository;

import faang.school.analytics.dto.Interval;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.criteria.AnalyticsGetCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnalyticsEventRepositoryCustomImpl implements AnalyticsEventRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnalyticsEvent> findByCriteria(@NonNull AnalyticsGetCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AnalyticsEvent> query = cb.createQuery(AnalyticsEvent.class);
        Root<AnalyticsEvent> analyticsEvent = query.from(AnalyticsEvent.class);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getReceiverId() != null) {
            predicates.add(cb.equal(analyticsEvent.get("receiverId"), criteria.getReceiverId()));
        }

        if (criteria.getEventType() != null) {
            predicates.add(cb.equal(analyticsEvent.get("eventType"), criteria.getEventType()));
        }

        if (criteria.getInterval() != null) {
            LocalDateTime fromDate = getStartOfInterval(criteria.getInterval());
            predicates.add(cb.greaterThanOrEqualTo(analyticsEvent.get("receivedAt"), fromDate));
        } else {
            if (criteria.getFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(analyticsEvent.get("receivedAt"), criteria.getFrom()));
            }

            if (criteria.getTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(analyticsEvent.get("receivedAt"), criteria.getTo()));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (criteria.getSortField() != null) {
            Path<?> sortPath = analyticsEvent.get(criteria.getSortField().getField());
            if (criteria.getSortDirection() == AnalyticsGetCriteria.SortDirection.DESC) {
                query.orderBy(cb.desc(sortPath));
            } else {
                query.orderBy(cb.asc(sortPath));
            }
        }

        TypedQuery<AnalyticsEvent> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private LocalDateTime getStartOfInterval(Interval interval) {
        return switch (interval){
            case LAST_DAY -> LocalDateTime.now().minusDays(1);
            case LAST_WEEK -> LocalDateTime.now().minusWeeks(1);
            case LAST_MONTH -> LocalDateTime.now().minusMonths(1);
            case LAST_YEAR -> LocalDateTime.now().minusYears(1);
        };
    }
}