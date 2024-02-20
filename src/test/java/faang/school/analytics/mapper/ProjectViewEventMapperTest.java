package faang.school.analytics.mapper;

import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.base.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventMapperTest {
    private final AnalyticsEventMapper<ProjectViewEvent> analyticsEventMapper = new ProjectViewEventMapperImpl();
    private final EventType eventType = EventType.PROJECT_VIEW;
    private final long receiverId = 2L;
    private final long actorId = 3L;
    private final AnalyticsEvent expectedAnalyticsEvent = AnalyticsEvent.builder()
            .eventType(eventType)
            .receiverId(receiverId)
            .actorId(actorId)
            .build();
    private final ProjectViewEvent projectViewEvent = ProjectViewEvent.builder()
            .projectId(receiverId)
            .ownerId(actorId)
            .build();

    @Test
    void testToAnalyticsEvent_argIsNull_returnsNull() {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(null);

        assertNull(analyticsEvent);
    }

    @Test
    void testToAnalyticsEvent_argIsNotNull_returnsAnalyticsEvent() {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(projectViewEvent);

        assertEquals(expectedAnalyticsEvent, analyticsEvent);
    }
}
