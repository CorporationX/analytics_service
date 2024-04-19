package faang.school.analytics.service;

import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    AnalyticsService analyticsService;

    private ProjectViewEvent projectViewEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setup() {
        projectViewEvent = ProjectViewEvent.builder()
                .userId(1L)
                .projectId(1L)
                .timestamp(LocalDateTime.now())
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(projectViewEvent.getUserId())
                .receiverId(projectViewEvent.getProjectId())
                .receivedAt(projectViewEvent.getTimestamp())
                .build();
    }

    @Test
    void savePostViewEvent_PostViewEventMappedToAnalyticsEvent_ThenSavedToDb() {
        analyticsService.saveEvent(analyticsEvent);

        Mockito.verify(analyticsEventRepository, times(1)).save(any(AnalyticsEvent.class));
    }
}