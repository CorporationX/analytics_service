package faang.school.analytics.service;

import faang.school.analytics.dto.SearchAppearanceEvent;
import org.springframework.data.redis.connection.Message;
import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {

    void saveSearchAppearanceEvent(SearchAppearanceEvent event);

    void handleSearchAppearanceFromBroker(Message message);

    void saveEvent(AnalyticsEvent event);
}
