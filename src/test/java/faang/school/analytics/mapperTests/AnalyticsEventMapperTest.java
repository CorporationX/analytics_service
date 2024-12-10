package faang.school.analytics.mapperTests;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsEventMapperTest {

    private final AnalyticsEventMapper mapper = new AnalyticsEventMapper();

    @Test
    void mapToLog() {
        SearchAppearanceEvent event = new SearchAppearanceEvent();
        event.setUserId(1L);
        event.setSearchingUserId(2L);
        event.setViewedAt(LocalDateTime.of(2024, 12, 4, 10, 30));

        String expectedLog = "User 1 viewed by user 2 at 2024-12-04T10:30";
        String actualLog = mapper.mapToLog(event);

        assertEquals(expectedLog, actualLog);
    }
}