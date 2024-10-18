package faang.school.analytics.service;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsEventService {

    @Transactional
    void saveEvent(AnalyticsEvent event);

    @Transactional(readOnly = true)
    List<AnalyticsEventDto> getAnalytics(Long receiverId,
                                         EventType eventType,
                                         Interval interval,
                                         LocalDateTime from,
                                         LocalDateTime to);
}