package faang.school.analytics.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostViewEvent(
        Long idPost,
        Long idUser,
        Long idAuthor,
        LocalDateTime date) {
}

