package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
                    Interval.parse(filterDto.getInterval()).contains(analyticsEvent.getReceivedAt()));
        } else {
            LocalDateTime to = filterDto.getTo();
            LocalDateTime from = filterDto.getFrom();

            if (from != null && to != null) {
                return analyticsEvents.filter(analyticsEvent ->
                        to.isAfter(analyticsEvent.getReceivedAt()) && from.isBefore(analyticsEvent.getReceivedAt()));
            } else {
                log.error("interval is null");
                throw new IllegalArgumentException("interval is null");
            }
        }
    }
}
