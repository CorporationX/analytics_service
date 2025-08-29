package faang.school.analytics;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.mapper.MentorshipEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository repository;

    @Mock
    private MentorshipEventMapper mapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void testShouldSaveEventWhenValidDto() {
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

        analyticsEventService.saveEvent(dto);

        verify(mapper).toEntity(dto);
        verify(repository).save(mappedEvent);
    }

    @Test
    void testShouldThrowExceptionWhenInvalidUserIds() {
        long invalidSenderId = 0;
        long invalidReceiverId = -5;
        LocalDateTime timestamp = LocalDateTime.now();

        MentorshipEventDto dto = new MentorshipEventDto(invalidSenderId, invalidReceiverId, timestamp);

        assertThatThrownBy(() -> analyticsEventService.saveEvent(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User IDs must be greater than zero");
    }
}