package ua.kazo.dentalacademy.exception;

import lombok.Getter;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;

import java.text.MessageFormat;

public class ApplicationException extends RuntimeException {

    @Getter
    private final ExceptionCode exceptionCode;

    public ApplicationException(ExceptionCode exceptionCode, Object... messageParams) {
        super(MessageFormat.format(exceptionCode.getMessage(), messageParams));
        this.exceptionCode = exceptionCode;
    }

}
