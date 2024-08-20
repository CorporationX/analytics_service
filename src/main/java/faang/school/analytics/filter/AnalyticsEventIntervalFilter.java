package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Slf4j
public class AnalyticsEventIntervalFilter implements AnalyticsEventFilter {
    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filterDto) {
        return filterDto.getInterval() != null || (filterDto.getFrom() != null && filterDto.getTo() != null);
    }

    @Override
    public Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> analyticsEvents, AnalyticsEventFilterDto filterDto) {
        if (filterDto.getInterval() != null) {
            return analyticsEvents.filter(analyticsEvent ->
                    filterDto.getInterval().contains(analyticsEvent.getReceivedAt().toDateTime()));
        } else {
            LocalDateTime to = filterDto.getTo();
            LocalDateTime from = filterDto.getFrom();

            if (from != null && to != null) {
                return analyticsEvents.filter(analyticsEvent ->
                        from.isAfter(analyticsEvent.getReceivedAt()) && to.isBefore(analyticsEvent.getReceivedAt()));
            } else {
                log.error("interval is null");
                throw new IllegalArgumentException("interval is null");
            }
        }
    }
}
