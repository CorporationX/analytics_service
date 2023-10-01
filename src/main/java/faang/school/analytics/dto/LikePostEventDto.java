package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikePostEventDto {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
}