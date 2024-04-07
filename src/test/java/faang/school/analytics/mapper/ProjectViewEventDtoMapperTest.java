package faang.school.analytics.mapper;

import faang.school.analytics.dto.ProjectViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventDtoMapperTest {
    private final AnalyticsEventMapper analyticsEventMapper = new AnalyticsEventMapperImpl();
    private final EventType eventType = EventType.PROJECT_VIEW;
    private final long receiverId = 2L;
    private final long actorId = 3L;
    private final AnalyticsEvent expectedAnalyticsEvent = AnalyticsEvent.builder()
            .eventType(eventType)
            .receiverId(receiverId)
            .actorId(actorId)
            .build();
    private final ProjectViewEventDto projectViewEventDto = ProjectViewEventDto.builder()
            .projectId(receiverId)
            .ownerId(actorId)
            .build();


    @Test
    void testToAnalyticsEvent_argIsNotNull_returnsAnalyticsEvent() {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(projectViewEventDto);

        assertEquals(expectedAnalyticsEvent, analyticsEvent);
    }
}
