package ua.kazo.dentalacademy.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.exception.RestError;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(final Exception e) {
        log.error("Exception", e);
        RestError restError = new RestError(e.getMessage());
        HttpStatus status;
        if (e instanceof ApplicationException) {
            status = ((ApplicationException) e).getExceptionCode().getStatus();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(restError);
    }

}
