package faang.school.analytics.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String aboutMe;
    private String email;
    private List<Long> menteeIds;
    private List<Long> mentorIds;
}
