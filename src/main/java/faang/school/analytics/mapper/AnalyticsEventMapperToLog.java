package faang.school.analytics.mapper;

import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.event.SearchAppearanceEvent;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapperToLog {

    public String mapToLog(SearchAppearanceEvent event) {
        return String.format(
                "User %d viewed by user %d at %s",
                event.getUserId(),
                event.getSearchingUserId(),
                event.getViewedAt()
        );
    }
    public String mapToLog(PremiumBoughtEvent event) {
        return String.format(
                "User %d viewed by user %d at %s",
                event.getUserId(),
                event.getAmount(),
                event.getDuration()
        );
    }
}