package faang.school.analytics.dto;

import lombok.Builder;

@Builder
public record UserDto(Long id, String username, String email, String phone, String aboutMe) {
}
