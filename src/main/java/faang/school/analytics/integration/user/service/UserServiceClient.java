package faang.school.analytics.integration.user.service;

import faang.school.analytics.integration.user.dto.UserResponseDto;

public interface UserServiceClient {
    UserResponseDto getUser(long id);
}
