package faang.school.analytics;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.exception.AnalyticsException;
import faang.school.analytics.repository.MentorshipEventRepository;
import faang.school.analytics.mapper.MentorshipEventMapper;
import faang.school.analytics.service.MentorshipEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MentorshipEventServiceTest {

    private MentorshipEventRepository repository;
    private MentorshipEventMapper mapper;
    private MentorshipEventService service;

    @BeforeEach
    void setUp() {
        repository = mock(MentorshipEventRepository.class);
        mapper = mock(MentorshipEventMapper.class);
        service = new MentorshipEventService(repository, mapper);
    }

    @Test
    void testGetProcessMentorshipEventValidEvent() {
        MentorshipEventDto dto = new MentorshipEventDto(1L, 2L, LocalDateTime.now());

        service.processMentorshipEvent(dto);

        verify(repository).save(dto);
    }

    @Test
    void testGetAnalyticsValidDateRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        List<MentorshipEventDto> result = service.getUserMentorshipAnalytics(1L, from, to);

        assertNull(result);
    }

    @Test
    void testProcessMentorshipEventInvalidSenderId() {
        MentorshipEventDto dto = new MentorshipEventDto(0L, 1L, LocalDateTime.now());

        assertThrows(AnalyticsException.class, () -> service.processMentorshipEvent(dto));
    }
}