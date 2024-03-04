package faang.school.analytics.mapper;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Alexander Bulgakov
 */
@ExtendWith(MockitoExtension.class)
public class AnalyticsEventMapperTest {
    private final AnalyticsEventMapper analyticsEventMapper =
            Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    public void testToAnalyticsEventWhenSearchAppearanceEvent() {
        SearchAppearanceEvent searchAppearanceEvent = new SearchAppearanceEvent(
                1L,
                2L,
                LocalDateTime.now()
        );

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(searchAppearanceEvent);

        assertNotNull(analyticsEvent,
                "The result should not be null when the input is not null");
    }

    @Test
    public void testToAnalyticsEventWhenSearchAppearanceEventNull() {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(null);

        assertNull(analyticsEvent,
                "The result should be null when the input is null");
    }
}
