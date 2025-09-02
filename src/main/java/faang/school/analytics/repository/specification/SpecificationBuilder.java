package faang.school.analytics.repository.specification;

import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.AnalyticsEvent_;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeIntervalType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Спецификация для {@link RecommendationFilterDto}
 *
 * @author Linempy
 * @since 21.08.2025
 */
public class SpecificationBuilder {

    public static Specification<AnalyticsEvent> buildSpecification(RecommendationFilterDto filterDto) {
        return Specification.allOf(byReceiverId(filterDto.id()),
                byEventType(filterDto.eventType()),
                byTimeIntervalType(filterDto.timeType()),
                byCreatedAtBetween(filterDto.startTime(), filterDto.endTime())
        );
    }

    public static Specification<AnalyticsEvent> byReceiverId(Long receiverId) {
        if (receiverId == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get(AnalyticsEvent_.RECEIVER_ID), receiverId);
    }

    public static Specification<AnalyticsEvent> byEventType(EventType type) {
        if (type == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get(AnalyticsEvent_.EVENT_TYPE), type);
    }

    public static Specification<AnalyticsEvent> byTimeIntervalType(TimeIntervalType intervalType) {
        if (intervalType == null) {
            return null;
        }

        LocalDateTime[] dateRange = intervalType.getRange();
        LocalDateTime startDate = dateRange[0];
        LocalDateTime endDate = dateRange[1];

        return byCreatedAtBetween(startDate, endDate);
    }

    public static Specification<AnalyticsEvent> byCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }

        return (root, query, cb) -> {
            if (startDate != null && endDate != null) {
                return cb.between(root.get(AnalyticsEvent_.RECEIVED_AT), startDate, endDate);
            } else if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get(AnalyticsEvent_.RECEIVED_AT), startDate);
            } else {
                return cb.lessThanOrEqualTo(root.get(AnalyticsEvent_.RECEIVED_AT), endDate);
            }
        };
    }


}