package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsEventMapperTest {
    private AnalyticsEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AnalyticsEventMapper.class);
    }

    @Test
    void postViewEventDtoToAnalyticsEvent() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 11, 12, 13, 15);
        PostViewEventDto dto = new PostViewEventDto(1L, 2L, 3L, dateTime);
        AnalyticsEvent expected = new AnalyticsEvent(0L, 2L, 3L, EventType.POST_VIEW, dateTime);
        AnalyticsEvent event = mapper.postViewEventDtoToAnalyticsEvent(dto);
//        assertEquals(expected, event);
        assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(event);
    }
}