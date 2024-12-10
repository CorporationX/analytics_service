package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;

import java.util.stream.Stream;

public class AnalyticsDateAndSortedFilter implements AnalyticsFilterI {
    @Override
    public boolean isApplicable(AnalyticsFilterDto analyticsFilterDto) {
        return analyticsFilterDto.getFrom() != null && analyticsFilterDto.getTo() != null;
    }

    @Override
    public Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> eventsStream, AnalyticsFilterDto analyticsFilterDto) {
        return eventsStream.filter(event ->
                !event.getReceivedAt().isBefore(analyticsFilterDto.getFrom()) &&
                        !event.getReceivedAt().isAfter(analyticsFilterDto.getTo()));
    }
}
