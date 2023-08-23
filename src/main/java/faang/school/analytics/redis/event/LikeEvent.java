package faang.school.analytics.redis.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEvent implements Serializable {
    private Long idPost;
    private Long idAuthor;
    private Long idUser;
    private LocalDateTime dateTime;

    public static LikeEvent fromString(String input) {
        // Удалить символ '}' из строки
        input = input.replace("}", "");

        LikeEvent likeEvent = new LikeEvent();
        String[] parts = input.split(", ");

        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "idPost":
                        likeEvent.setIdPost(Long.parseLong(value));
                        break;
                    case "idAuthor":
                        likeEvent.setIdAuthor(Long.parseLong(value));
                        break;
                    case "idUser":
                        likeEvent.setIdUser(Long.parseLong(value));
                        break;
                    case "dateTime":
                        likeEvent.setDateTime(LocalDateTime.parse(value));
                        break;
                }
            }
        }
        return likeEvent;
    }
}