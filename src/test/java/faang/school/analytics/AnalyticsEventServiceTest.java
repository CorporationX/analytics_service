package faang.school.analytics;

import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.hibernate.sql.ast.tree.expression.Over;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void saveEvent() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEventService.saveEvent(analyticsEvent);
        Mockito.verify(analyticsEventRepository, Mockito.times(1))
                .save(analyticsEvent);
    }
}
