package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsEventMapperTest {
    private final AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    void mapMentorshipRequestEventToAnalyticsEvent() {
        MentorshipRequestEvent mentorshipRequestEvent = new MentorshipRequestEvent(1L, 2L, null);

        AnalyticsEvent result = mapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent);

        assertNotNull(result);
        assertEquals(1L, result.getReceiverId());
        assertEquals(2L, result.getActorId());
        assertNull(result.getReceivedAt());
    }
}
