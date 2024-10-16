package faang.school.analytics.mapper;

import faang.school.analytics.dto.message.MentorshipRequestMessage;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestMapperTest {

    @InjectMocks
    private MentorshipRequestMapperImpl mapper;

    @Test
    @DisplayName("success mapping MentorshipRequestMessage to AnalyticsEvent")
    void testToAnalyticsEvent(){
        var message = MentorshipRequestMessage.builder()
                .requesterId(1)
                .receiverId(2)
                .createdAt(LocalDateTime.of(1,11, 11, 22, 33))
                .build();

        AnalyticsEvent event = mapper.toAnalyticsEvent(message);

        assertEquals(event.getActorId(), message.getRequesterId());
        assertEquals(event.getReceiverId(), message.getReceiverId());
        assertEquals(event.getReceivedAt(), message.getCreatedAt());
    }

}