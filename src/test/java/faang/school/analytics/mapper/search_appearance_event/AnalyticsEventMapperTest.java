package faang.school.analytics.mapper.search_appearance_event;

import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AnalyticsEventMapperTest {
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    private SearchAppearanceEventDto searchAppearanceEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        searchAppearanceEventDto = SearchAppearanceEventDto.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(now)
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(now)
                .build();

    }

    @Test
    void testToEntity() {
        AnalyticsEvent entity = analyticsEventMapper.toEntity(searchAppearanceEventDto);
        assertEquals(analyticsEvent, entity);
    }

    @Test
    void testToDto() {
        SearchAppearanceEventDto dto = analyticsEventMapper.toDto(analyticsEvent);
        assertEquals(searchAppearanceEventDto, dto);
    }
}