package faang.school.analytics.mapper.mentorship;

import faang.school.analytics.dto.event.mentorship.MentorshipRequestEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MentorshipRequestEventMapperTest {

    private MentorshipRequestEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(MentorshipRequestEventMapper.class);
    }

    @Test
    void mapsMentorshipRequestEventToAnalyticsEvent() {
        var eventId = UUID.randomUUID();
        MentorshipRequestEvent mentorshipRequestEvent = MentorshipRequestEvent.builder()
                .requesterId(123)
                .eventId(eventId)
                .receiverId(456)
                .timestamp(LocalDateTime.of(2023, 10, 1, 12, 0))
                .build();
        var analyticsEvent = AnalyticsEvent.builder()
                .eventId(eventId)
                .actorId(123)
                .receiverId(456)
                .receivedAt(LocalDateTime.of(2023, 10, 1, 12, 0))
                .build();

        var result = mapper.toAnalyticsEntity(mentorshipRequestEvent);

        assertEquals(analyticsEvent, result);
    }
}