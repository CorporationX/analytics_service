package faang.school.analytics.service;

import faang.school.analytics.dto.RecomendationEventDto;
import faang.school.analytics.mapper.RecomendationEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecomendationServiceTest {
    @InjectMocks
    private RecomendationAnalytics recomendationAnalytics;
    @Spy
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private RecomendationEventMapper recomendationEventMapper;


    @Test
    public void recomendationAnalyticsSave(){
        RecomendationEventDto recomendationEventDto = RecomendationEventDto.builder().build();
        when(recomendationEventMapper.toEntity(recomendationEventDto)).thenReturn(new AnalyticsEvent());
        when(analyticsEventRepository.save(any())).thenReturn(any());
        recomendationAnalytics.save(recomendationEventDto);
        Assertions.assertEquals(true,recomendationAnalytics.supportsEventType(recomendationEventDto));
        Mockito.verify(analyticsEventRepository).save(any());
    }
}
