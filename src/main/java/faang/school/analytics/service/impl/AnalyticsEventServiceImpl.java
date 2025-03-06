package faang.school.analytics.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public void saveSearchAppearanceEvent(SearchAppearanceEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("event is null");
        }
        analyticsEventRepository.save(mapper.mapSearchAppearanceToAnalyticEvent(event));
    }

    @Override
    public void handleSearchAppearanceFromBroker(Message message) {
        try {
            String json = objectMapper.readValue(message.getBody(), String.class);
            SearchAppearanceEvent event = objectMapper.readValue(json, SearchAppearanceEvent.class);
            saveSearchAppearanceEvent(event);
        } catch (Exception e) {
            log.error("error when saving analytics", e);
        }
    }
}
