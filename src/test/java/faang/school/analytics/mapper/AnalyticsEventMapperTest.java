package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestReceivedDto;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static faang.school.analytics.model.EventType.MENTORSHIP_REQUEST_RECEIVED;
import static faang.school.analytics.model.EventType.POST_VIEW;
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
        AnalyticsEvent expected = new AnalyticsEvent(0L, 2L, 3L, POST_VIEW, dateTime);
        AnalyticsEvent event = mapper.postViewEventDtoToAnalyticsEvent(dto);
        assertEquals(expected, event);
    }

    @Test
    void mentorshipRequestReceivedDtoToAnalyticsEvent() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 11, 12, 13, 15);
        MentorshipRequestReceivedDto dto = new MentorshipRequestReceivedDto(1L, 2L, 3L, dateTime);
        AnalyticsEvent expected = new AnalyticsEvent(0L, 2L, 3L, MENTORSHIP_REQUEST_RECEIVED, dateTime);
        AnalyticsEvent event = mapper.mentorshipRequestReceivedDtoToAnalyticsEvent(dto);
        assertEquals(expected, event);
    }
}