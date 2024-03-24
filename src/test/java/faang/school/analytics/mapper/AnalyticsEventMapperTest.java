package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.SearchAppearanceEventDto;
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
        SearchAppearanceEventDto searchAppearanceEventDto = new SearchAppearanceEventDto(
                1L,
                2L,
                LocalDateTime.now()
        );

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(searchAppearanceEventDto);

        assertNotNull(analyticsEvent,
                "The result should not be null when the input is not null");
    }

    @Test
    public void testToAnalyticsEventWhenSearchAppearanceEventNull() {
        SearchAppearanceEventDto searchAppearanceEventDto = null;
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent((SearchAppearanceEventDto) null);

        assertNull(analyticsEvent,
                "The result should be null when the input is null");
    }
}
