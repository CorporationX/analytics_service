package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.util.EnumConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.stream.Stream;

@Component
@Slf4j
public class AnalyticsEventIntervalFilter implements AnalyticsEventFilter {
    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filterDto) {
        return filterDto.getInterval() != null || (filterDto.getFrom() != null && filterDto.getTo() != null);
    }

    @Override
    public Stream<AnalyticsEvent> filter(Stream<AnalyticsEvent> analyticsEvents,
                                         AnalyticsEventFilterDto analyticsEventFilterDto) {
        if(!isApplicable(analyticsEventFilterDto)){
            String errMessage = "There should be present interval or time bounds";
            log.error(errMessage);
            throw new IllegalArgumentException(errMessage);
        }

        Interval interval = EnumConverter.fromValue(Interval.class, analyticsEventFilterDto.getInterval());
        LocalDateTime from = analyticsEventFilterDto.getFrom();
        LocalDateTime to = analyticsEventFilterDto.getTo();

        if(interval != null){
            return analyticsEvents.filter(analyticsEvent -> analyticsEvent.getReceivedAt()
                    .plusDays(interval.getDays()).isAfter(LocalDateTime.now()));
        } else {
            return analyticsEvents.filter(analyticsEvent ->
                    to.isAfter(analyticsEvent.getReceivedAt()) && from.isBefore(analyticsEvent.getReceivedAt()));
        }
    }
}
