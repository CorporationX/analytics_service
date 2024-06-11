package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    private AnalyticsEvent analyticsEvent;
    private AnalyticsEvent analyticsEvent2;
    private PostViewEvent postViewEvent;

    @BeforeEach
    void setUp() {
        analyticsEvent = new AnalyticsEvent();
        analyticsEvent2 = new AnalyticsEvent();
        postViewEvent = new PostViewEvent();
    }

    @Test
    @DisplayName("Save event.")
    public void testSaveEvent() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent2);
        when(analyticsEventMapper.toEntity(postViewEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveEvent(postViewEvent);

        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
