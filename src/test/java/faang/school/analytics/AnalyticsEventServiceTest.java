package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private final AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    @Mock
    private AnalyticsEventValidator analyticsEventValidator;
    @InjectMocks
    AnalyticsEventService analyticsEventService;

    AnalyticsEventDto analyticsEventDto;
    AnalyticsEvent analyticsEvent;

    @BeforeEach
    void init() {
        analyticsEventDto = AnalyticsEventDto.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(2L)
                .eventType("POST_LIKE")
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(2L)
                .eventType(EventType.POST_LIKE)
                .build();
    }

    @Test
    @Named("Test saveEvent")
    public void testSaveEvent(){
        when(analyticsEventRepository.save(any())).thenReturn(analyticsEvent);
        AnalyticsEventDto result = analyticsEventService.saveEvent(analyticsEventDto);
        assertEquals(1L, result.getId());
    }


}
