package faang.school.analytics.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime timestamp;

    private String error;
    private int status;
    private String message;
    private String url;

    public ErrorResponseDto(String message){
        this.message = message;
    }
}