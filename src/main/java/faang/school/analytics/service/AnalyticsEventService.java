package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.NotFoundException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEvent analyticsEvent) {

        AnalyticsEvent saveAnalyticsEvent = analyticsEventRepository.save(analyticsEvent);
        return analyticsEventMapper.toDto(saveAnalyticsEvent);
    }

    public List<AnalyticsEventDto> getAnalytics(Long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> findAnalyticsEventStream =
                analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if(findAnalyticsEventStream == null) {
            throw new NotFoundException("AnalyticsEvent with receiverId = " + receiverId +
                    " and eventType " + eventType +" does not found");
        }

        List<AnalyticsEvent> filterAnalyticsEvent = new ArrayList<>();

        if (interval == null) {

            filterAnalyticsEvent = findAnalyticsEventStream
                    .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(from))
                    .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isBefore(to))
                    .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                    .toList();
        } else {

            Stream<AnalyticsEvent> filterAnalyticsEventStream = switch (interval) {
                case DAY -> findAnalyticsEventStream.filter(
                        analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusDays(1)));
                case WEEK -> findAnalyticsEventStream.filter(
                        analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusWeeks(1)));
                case MONTH -> findAnalyticsEventStream.filter(
                        analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusMonths(1)));
                case THREE_MONTH -> findAnalyticsEventStream.filter(
                        analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusMonths(3)));
                case SIX_MONTH -> findAnalyticsEventStream.filter(
                        analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusMonths(6)));
                case YEAR -> findAnalyticsEventStream.filter(
                        analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusYears(1)));
            };

            filterAnalyticsEvent =
                    filterAnalyticsEventStream
                            .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                            .toList();
        }

        return analyticsEventMapper.toDtoList(filterAnalyticsEvent);
    }
}

//    public List<AnalyticsEventDto> getAnalytics(Long receiverId, EventType eventType, String interval) {
//
//        Stream<AnalyticsEvent> findAnalyticsEventStream =
//                analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
//        Interval foundInterval = Interval.valueOf(interval.toUpperCase());
//
//        Stream<AnalyticsEvent> filterAnalyticsEventStream = switch (foundInterval) {
//            case DAY -> findAnalyticsEventStream.filter(
//                    analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusDays(1)));
//            case WEEK -> findAnalyticsEventStream.filter(
//                    analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusWeeks(1)));
//            case MONTH -> findAnalyticsEventStream.filter(
//                    analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusMonths(1)));
//            case THREE_MONTH -> findAnalyticsEventStream.filter(
//                    analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusMonths(3)));
//            case SIX_MONTH -> findAnalyticsEventStream.filter(
//                    analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusMonths(6)));
//            case YEAR -> findAnalyticsEventStream.filter(
//                    analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now().minusYears(1)));
//        };
//
//        List<AnalyticsEvent> filterAnalyticsEvent =
//                filterAnalyticsEventStream
//                        .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
//                        .toList();
//
//        return analyticsEventMapper.toDtoList(filterAnalyticsEvent);
//    }
//
//    public List<AnalyticsEventDto> getAnalytics(Long receiverId, EventType eventType,
//                                                String startDate, String finishDate) throws ParseException {
//
//        Stream<AnalyticsEvent> findAnalyticsEventStream =
//                analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
//
////        LocalDate dateStart = LocalDate.parse(startDate);
////        LocalDate dateFinish = LocalDate.parse(finishDate);
//
//        LocalDateTime dateStart = LocalDateTime.parse(startDate);
//        LocalDateTime dateFinish = LocalDateTime.parse(finishDate);
//
//        List<AnalyticsEvent> filterAnalyticsEvent = findAnalyticsEventStream
//                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(dateStart))
//                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isBefore(dateFinish))
//                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
//                .toList();
//
//        return analyticsEventMapper.toDtoList(filterAnalyticsEvent);
//    }
//}