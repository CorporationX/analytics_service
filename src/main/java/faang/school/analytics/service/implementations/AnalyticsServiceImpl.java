package faang.school.analytics.service.implementations;

import faang.school.analytics.dto.AnalyticsCreateEventDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.GetAnalyticsRqDto;
import faang.school.analytics.dto.IntervalDto;
import faang.school.analytics.dto.Period;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsEventRepository analyticsRepository;
    private final AnalyticsMapper analyticsMapper;

    @Override
    public AnalyticsEventDto saveEvent(AnalyticsCreateEventDto analyticsCreateEventDto) {
        AnalyticsEvent newAnalyticsEvent = analyticsMapper.toEntity(analyticsCreateEventDto);
        newAnalyticsEvent.setReceivedAt(LocalDateTime.now());
        AnalyticsEvent savedEvent = analyticsRepository.save(newAnalyticsEvent);

        return analyticsMapper.toDto(savedEvent);
    }

    public AnalyticsEvent addAnalyticsEvent(AnalyticsEvent analyticsEvent) {
        return analyticsRepository.save(analyticsEvent);
    }

    @Override
    @Transactional
    public List<AnalyticsEventDto> getAnalytics(GetAnalyticsRqDto getAnalyticsRqDto) {
        Stream<AnalyticsEvent> analyticsEventList = analyticsRepository.findByReceiverIdAndEventType(
                getAnalyticsRqDto.receiverId(), getAnalyticsRqDto.eventTypeDto().toEventType());
        Period filterPeriod = generatePeriod(getAnalyticsRqDto.intervalDto(), getAnalyticsRqDto.from(), getAnalyticsRqDto.to(),
                getAnalyticsRqDto.count());
        return analyticsEventList
                .filter(event -> {
                    if (filterPeriod.beginDate() != null && filterPeriod.endDate() != null) {
                        return !filterPeriod.beginDate().isAfter(event.getReceivedAt()) &&
                                !filterPeriod.endDate().isBefore(event.getReceivedAt());
                    }
                    return true;
                })
                .sorted((a, b) -> b.getReceivedAt().compareTo(a.getReceivedAt()))
                .map(analyticsMapper::toDto)
                .toList();
    }

    private Period generatePeriod(IntervalDto intervalDto, LocalDateTime from, LocalDateTime to, Integer count) {
        if (intervalDto != null) {
            int param = count == null ? 1 : count;
            LocalDateTime endDate = LocalDateTime.now();

            LocalDateTime beginDate = switch (intervalDto) {
                case DAY -> endDate.minusDays(param);
                case WEEK -> endDate.minusWeeks(param);
                case MONTH -> endDate.minusMonths(param);
                case YEAR -> endDate.minusYears(param);
            };

            return new Period(beginDate, endDate);
        }

        if (from != null && to != null) {
            return new Period(from, to);
        }

        return new Period(null, null);
    }
}
