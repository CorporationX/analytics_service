package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostViewEvent {
    private Long idPost;
    private Long idUser;
    private Long idAuthor;
    private LocalDateTime date;
}

