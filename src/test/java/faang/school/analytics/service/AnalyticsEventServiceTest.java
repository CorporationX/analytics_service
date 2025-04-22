package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp() {
        analyticsEvent = new AnalyticsEvent();
    }

    @Test
    @DisplayName("Проверка успешного сохранения аналитики")
    public void givenValidData_whenSave_thenSuccessSave() {
        analyticsEventService.save(analyticsEvent);

        verify(analyticsEventRepository).save(analyticsEvent);
    }
}