package ua.kazo.dentalacademy.exception;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;

public class ApplicationException extends RuntimeException {

    @Getter
    private final ExceptionCode exceptionCode;

    public ApplicationException(MessageSource messageSource, ExceptionCode exceptionCode, Object... messageParams) {
        super(messageSource.getMessage(exceptionCode.getMessage(), messageParams, LocaleContextHolder.getLocale()));
        this.exceptionCode = exceptionCode;
    }

}
