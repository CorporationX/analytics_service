package faang.school.analytics.integration.project.service;

import faang.school.analytics.integration.project.dto.ProjectResponseDto;

public interface ProjectServiceClient {
    ProjectResponseDto getProject(long id);
}
