package faang.school.analytics.mapper;

import faang.school.analytics.event.SearchAppearanceEvent;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapper {

    public String mapToLog(SearchAppearanceEvent event) {
        return String.format(
                "User %d viewed by user %d at %s",
                event.getUserId(),
                event.getSearchingUserId(),
                event.getViewedAt()
        );
    }
}