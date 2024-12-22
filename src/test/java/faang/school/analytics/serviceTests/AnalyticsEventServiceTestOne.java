package faang.school.analytics.serviceTests;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.mappers.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
class AnalyticsEventServiceTestOne {

    @MockBean
    private AnalyticsEventRepository analyticsEventRepository;

    @MockBean
    private AnalyticsEventMapper analyticsEventMapper;

    @Autowired
    private AnalyticsEventService analyticsEventService;

    @Test
    void testProcessRecommendationEvent() {
        RecommendationEvent event = new RecommendationEvent(1L, 2L, 3L, LocalDateTime.now());
        AnalyticsEventDto dto = AnalyticsEventDto.builder()
                .id(1L)
                .actorId(2L)
                .receiverId(3L)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();
        AnalyticsEvent entity = AnalyticsEvent.builder()
                .id(1L)
                .actorId(2L)
                .receiverId(3L)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();


        when(analyticsEventMapper.toEntity(dto)).thenReturn(entity);


        when(analyticsEventMapper.toEntity(any(AnalyticsEventDto.class))).thenReturn(entity);


        analyticsEventService.processRecommendationEvent(event);


        verify(analyticsEventRepository, times(1)).save(entity);
    }
}
