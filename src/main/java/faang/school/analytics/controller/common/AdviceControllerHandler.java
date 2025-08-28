package faang.school.analytics.controller.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static faang.school.analytics.controller.common.ApiExceptionDto.ErrorType.SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class AdviceControllerHandler {

    @ExceptionHandler({IllegalArgumentException.class, ArrayIndexOutOfBoundsException.class,
            IllegalStateException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiExceptionDto onBaseExceptionsHandle(final Exception e) {
        log.error(e.getMessage());
        ApiExceptionDto apiExceptionDto = new ApiExceptionDto();
        apiExceptionDto.setMessage(e.getMessage());
        apiExceptionDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiExceptionDto.setTimestamp(System.currentTimeMillis());
        apiExceptionDto.setErrorType(SERVER_ERROR);
        return apiExceptionDto;
    }

}
