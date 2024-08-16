package faang.school.analytics.mapper.project;

import faang.school.analytics.dto.event.project.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProjectViewEventMapperTest {

    private final ProjectViewEventMapper mapper = Mappers.getMapper(ProjectViewEventMapper.class);

    @Test
    void toAnalyticsEntity_mapsFieldsCorrectly() {
        ProjectViewEvent projectViewEvent = ProjectViewEvent.builder()
                .eventId(UUID.randomUUID())
                .viewerId(123)
                .projectId(456)
                .timestamp(LocalDateTime.of(2023, 10, 1, 12, 0))
                .build();

        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEntity(projectViewEvent);

        assertEquals(projectViewEvent.getEventId(), analyticsEvent.getEventId());
        assertEquals(projectViewEvent.getViewerId(), analyticsEvent.getActorId());
        assertEquals(projectViewEvent.getProjectId(), analyticsEvent.getReceiverId());
        assertEquals(projectViewEvent.getTimestamp(), analyticsEvent.getReceivedAt());
    }
}