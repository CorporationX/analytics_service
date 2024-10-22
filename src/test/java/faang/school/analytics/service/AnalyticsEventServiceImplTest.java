package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.LikeEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {
    @InjectMocks
    private AnalyticsEventServiceImpl service;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    private LikeEvent likeEvent;
    private AnalyticsEvent analyticsEvent;
    @BeforeEach
    void setUp() {
        likeEvent = new LikeEvent();
        analyticsEvent = new AnalyticsEvent();
    }
    @Test
    void saveLikeEvent() {
        when(analyticsEventMapper.toEntity(likeEvent)).thenReturn(analyticsEvent);

        service.saveLikeEvent(likeEvent);

        verify(analyticsEventRepository,times(1)).save(analyticsEvent);
    }
}
