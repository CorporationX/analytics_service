package faang.school.analytics.service;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    AnalyticsEventService analyticsEventService;
    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    AnalyticsEventMapper analyticsEventMapper;

    @BeforeEach
    void setUp() {
        analyticsEventMapper = new AnalyticsEventMapperImpl();
        analyticsEventService = new AnalyticsEventService(analyticsEventRepository,analyticsEventMapper);
    }

    @Test
    void saveEvent() {
        EventDto eventDto = new EventDto();
        analyticsEventService.saveEvent(eventDto);
        verify(analyticsEventRepository).save(analyticsEventMapper.toModel(eventDto));
    }
}