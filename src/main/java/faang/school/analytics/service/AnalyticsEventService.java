package faang.school.analytics.service;

import faang.school.analytics.dto.SearchAppearanceEvent;
import org.springframework.data.redis.connection.Message;

public interface AnalyticsEventService {

    void saveSearchAppearanceEvent(SearchAppearanceEvent event);

    void handleSearchAppearanceFromBroker(Message message);
}
