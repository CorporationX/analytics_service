package faang.school.analytics.service;

import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface AnalyticsEventService {

    void saveSearchAppearanceEvent(SearchAppearanceEvent event);
}
