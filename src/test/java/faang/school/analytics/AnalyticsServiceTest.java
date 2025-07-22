package faang.school.analytics;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.exception.AnalyticsException;
import faang.school.analytics.exception.Exceptional;
import faang.school.analytics.repository.MentorshipEventRepository;
import faang.school.analytics.mapper.MentorshipEventMapper;
import faang.school.analytics.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    private MentorshipEventRepository repository;
    private MentorshipEventMapper mapper;
    private AnalyticsService service;

    @BeforeEach
    void setUp() {
        repository = mock(MentorshipEventRepository.class);
        mapper = mock(MentorshipEventMapper.class);
        service = new AnalyticsService(repository, mapper);
    }

    @Test
    void processMentorshipEventValidEventSavesEvent() {
        MentorshipEventDto dto = new MentorshipEventDto(1L, 2L, LocalDateTime.now());

        service.processMentorshipEvent(dto);

        verify(repository).save(dto);
    }

    @Test
    void getAnalyticsValidDateRangeReturnsNull() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        List<MentorshipEventDto> result = service.getAnalytics(1L, from, to);

        assertNull(result);
    }

    @Test
    void processMentorshipEventInvalidSenderIdThrowsAnalyticsException() {
        MentorshipEventDto dto = new MentorshipEventDto(0L, 1L, LocalDateTime.now());

        assertThrows(AnalyticsException.class, () -> service.processMentorshipEvent(dto));
    }
}