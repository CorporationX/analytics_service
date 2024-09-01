package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

    private String message;
    private int code;
}
