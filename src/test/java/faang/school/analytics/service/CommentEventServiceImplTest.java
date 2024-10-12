package faang.school.analytics.service;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentEventServiceImplTest {
    @InjectMocks
    private CommentEventServiceImpl service;
    @Mock
    private AnalyticsEventRepository repository;
    @Mock
    private AnalyticsEventMapper mapper;

    private CommentEvent event;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void init() {
        event = new CommentEvent();
        analyticsEvent = new AnalyticsEvent();

        Mockito.when(mapper.toAnalytics(event))
                .thenReturn(analyticsEvent);
    }

    @Test
    void save_whenOk() {
        service.save(event);

        Mockito.verify(mapper, Mockito.times(1))
                .toAnalytics(event);
        Mockito.verify(repository, Mockito.times(1))
                .save(analyticsEvent);
    }
}
