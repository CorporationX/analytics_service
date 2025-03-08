package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.dto.AnalyticsEventRequestDTO;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(AnalyticsEventDTO eventDTO) {
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDTO);
        analyticsEventRepository.save(event);
        log.info("Event {} was saved in the data base", event.getEventType());
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDTO> getAnalytics(AnalyticsEventRequestDTO analyticsEventRequestDTO) {
        log.info("Getting analytics by receiver id: {}, type event: {}", analyticsEventRequestDTO.receiverId(),
                analyticsEventRequestDTO.eventType());
        return analyticsEventRepository.findByReceiverIdAndEventType(analyticsEventRequestDTO.receiverId(),
                        analyticsEventRequestDTO.eventType())
                .filter(event ->
                        !event.getReceivedAt().isBefore(analyticsEventRequestDTO.from()) && !event.getReceivedAt()
                                .isAfter(analyticsEventRequestDTO.to()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}
