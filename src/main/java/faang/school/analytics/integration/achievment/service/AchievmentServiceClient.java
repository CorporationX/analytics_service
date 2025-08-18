package faang.school.analytics.integration.achievment.service;

import faang.school.analytics.integration.achievment.dto.AchievmentResponseDto;

public interface AchievmentServiceClient {
    AchievmentResponseDto getAchievment(long id);
}
