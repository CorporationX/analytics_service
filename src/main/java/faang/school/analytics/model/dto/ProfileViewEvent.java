package faang.school.analytics.model.dto;

import java.time.LocalDateTime;

public record ProfileViewEvent(long idRequester, long idUser, LocalDateTime createdTime) {

}
