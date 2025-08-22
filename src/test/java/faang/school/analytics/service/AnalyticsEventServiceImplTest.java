package faang.school.analytics.service;


import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository repository;

    @InjectMocks
    AnalyticsEventServiceImpl service;

    @Test
    @DisplayName("Проверяет, что метод save вызвался")
    public void saveEvent_SuccessfulSave() {
        AnalyticsEvent event = new AnalyticsEvent(
                1L,
                2L,
                3L,
                EventType.RECOMMENDATION_RECEIVED,
                LocalDateTime.now()
        );

        when(repository.save(event)).thenReturn(event);

        service.saveEvent(event);

        verify(repository, times(1)).save(event);
    }

    @Test
    @DisplayName("Проверяет успешный вызов метода репозитория")
    public void getAnalytics_SuccessfulVerify() {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                1L,
                EventType.RECOMMENDATION_RECEIVED,
                null,
                null,
                null);

        List<AnalyticsEvent> expectedEvents = List.of(
                new AnalyticsEvent(
                        1L,
                        100L,
                        101L,
                        EventType.RECOMMENDATION_RECEIVED,
                        LocalDateTime.now()
                )
        );

        when(repository.findByFilter(filterDto)).thenReturn(expectedEvents);

        service.getAnalytics(filterDto);

        verify(repository, times(1)).findByFilter(filterDto);
    }
}