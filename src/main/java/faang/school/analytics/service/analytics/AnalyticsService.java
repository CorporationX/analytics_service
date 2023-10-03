package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticsIntervalDto;
import faang.school.analytics.exeption.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Intervals;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    @Setter
    @Value("${analytics.latest_events_period_of_time}")
    private String latestPeriodOfTime;

    private static final String INTERVAL_PATTERN = "^([0-9]*)([a-zA-Z]+)$";
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEvent create(AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticEventDto> getLatestEvents() {
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.getLatestEvents(latestPeriodOfTime);
        return analyticsEvents.stream().map(analyticsEventMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<AnalyticEventDto> getAnalytics(AnalyticsIntervalDto analyticsIntervalDto) {
        List<AnalyticsEvent> getEvents;

        if (analyticsIntervalDto.getInterval() != null) {
            LocalDateTime startDate = calculateStartDate(analyticsIntervalDto.getInterval());
            getEvents = analyticsEventRepository.getEventsByInterval
                    (startDate, analyticsIntervalDto.getReceiverId(), analyticsIntervalDto.getEventType().toString());
        } else if (analyticsIntervalDto.getFrom() != null && analyticsIntervalDto.getTo() != null) {
            getEvents = analyticsEventRepository.getEventsByDates
                    (analyticsIntervalDto.getFrom(), analyticsIntervalDto.getTo(),
                            analyticsIntervalDto.getReceiverId(), analyticsIntervalDto.getEventType().toString());
        } else {
            throw new DataValidationException("Search data not specified");
        }

        return getEvents.stream().map(analyticsEventMapper::toDto).toList();
    }

    private LocalDateTime calculateStartDate(String interval) {
        Pattern pattern = Pattern.compile(INTERVAL_PATTERN);
        Matcher matcher = pattern.matcher(interval);

        if (matcher.matches()) {
            String amountStr = matcher.group(1);
            int amount = amountStr.isEmpty() ? 1 : Integer.parseInt(amountStr);
            String intervalType = matcher.group(2);
            try {
                Intervals intervals = Intervals.valueOf(intervalType.toUpperCase());
                return intervals.dateFrom(LocalDateTime.now(), amount);
            } catch (RuntimeException e) {
                throw new DataValidationException("Invalid interval type: " + interval);
            }
        } else {
            throw new DataValidationException("Invalid interval format: " + interval);
        }
    }
}
