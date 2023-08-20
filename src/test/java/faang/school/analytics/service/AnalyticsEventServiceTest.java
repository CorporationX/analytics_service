package faang.school.analytics.service;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    AnalyticsEventService analyticsEventService;
    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    @Mock
    AnalyticsEventMapper analyticsEventMapper;

    @Test
    void saveEvent() {
        analyticsEventService.saveEvent(new EventDto());
        verify(analyticsEventRepository).save(analyticsEventMapper.toModel(new EventDto()));
    }
}