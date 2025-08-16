package faang.school.analytics;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.mapper.MentorshipEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.MentorshipEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorshipEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;

    @Mock
    private MentorshipEventMapper mapper;

    @InjectMocks
    private MentorshipEventService mentorshipEventService;

    @Captor
    private ArgumentCaptor<AnalyticsEvent> eventCaptor;

    @Test
    void shouldSaveMentorshipEvent_whenValidDto() {
        long senderId = 1L;
        long receiverId = 2L;
        LocalDateTime timestamp = LocalDateTime.now();
        MentorshipEventDto dto = new MentorshipEventDto(senderId, receiverId, timestamp);

        AnalyticsEvent mappedEvent = AnalyticsEvent.builder()
                .actorId(senderId)
                .receiverId(receiverId)
                .receivedAt(timestamp)
                .eventType(faang.school.analytics.model.EventType.PROJECT_INVITE)
                .build();

        when(mapper.toEntity(dto)).thenReturn(mappedEvent);

        mentorshipEventService.saveEvent(dto);

        verify(mapper).toEntity(dto);
        verify(repository).save(eventCaptor.capture());

        AnalyticsEvent capturedEvent = eventCaptor.getValue();
        org.assertj.core.api.Assertions.assertThat(capturedEvent)
                .hasFieldOrPropertyWithValue("actorId", senderId)
                .hasFieldOrPropertyWithValue("receiverId", receiverId)
                .hasFieldOrPropertyWithValue("receivedAt", timestamp)
                .hasFieldOrPropertyWithValue("eventType", faang.school.analytics.model.EventType.PROJECT_INVITE);
    }
}