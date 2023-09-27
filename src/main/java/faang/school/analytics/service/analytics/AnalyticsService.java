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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    @Setter
    @Value("${analytics.latest_events_period_of_time}")
    private String latestPeriodOfTime;

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
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType
                (analyticsIntervalDto.getReceiverId(), analyticsIntervalDto.getEventType()).toList();
        List<AnalyticsEvent> getEvents = new ArrayList<>();

        if (analyticsIntervalDto.getInterval() != null) {
            LocalDateTime startDate = calculateStartDate(analyticsIntervalDto.getInterval());
            getEvents = analyticsEvents.stream().filter(analyticsEvent ->
                    analyticsEvent.getReceivedAt().isAfter(startDate)).toList();
        } else if (analyticsIntervalDto.getFrom() != null && analyticsIntervalDto.getTo() != null) {
            getEvents = analyticsEvents.stream().filter(analyticsEvent ->
                    analyticsEvent.getReceivedAt().isAfter(analyticsIntervalDto.getFrom()) &&
                            analyticsEvent.getReceivedAt().isBefore(analyticsIntervalDto.getTo())).toList();
        }

        return getEvents.stream()
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private LocalDateTime calculateStartDate(String interval) {
        Pattern pattern = Pattern.compile("^([0-9]*)([a-zA-Z]+)$");
        Matcher matcher = pattern.matcher(interval);

        if (matcher.matches()) {
            String amountStr = matcher.group(1);
            int amount = amountStr.isEmpty() ? 1 : Integer.parseInt(amountStr);
            String intervalType = matcher.group(2);
            try {
                Intervals intervals = Intervals.valueOf(intervalType.toUpperCase());
                return intervals.dateFrom(LocalDateTime.now(), amount);
            } catch (Exception e) {
                throw new DataValidationException("Invalid interval type: " + interval);
            }
        } else {
            throw new DataValidationException("Invalid interval format: " + interval);
        }
    }
}
