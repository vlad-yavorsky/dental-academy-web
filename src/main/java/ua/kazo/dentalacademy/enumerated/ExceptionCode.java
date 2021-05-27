package ua.kazo.dentalacademy.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ExceptionCode {

    FOLDER_ITEM_TYPE_NOT_EXIST(HttpStatus.NOT_FOUND, "exception.folderItem.TypeNotExist"),
    FOLDER_CATEGORY_NOT_EXIST(HttpStatus.NOT_FOUND, "exception.folder.CategoryNotExist"),
    USER_ROLE_NOT_EXIST(HttpStatus.NOT_FOUND, "exception.user.RoleNotExist"),
    INSUFFICIENT_PRIVILEGES(HttpStatus.FORBIDDEN, "exception.user.InsufficientPrivileges"),
    FOLDER_IS_EMPTY(HttpStatus.I_AM_A_TEAPOT, "exception.folder.Empty"),
    OLD_PASSWORD_IS_WRONG(HttpStatus.BAD_REQUEST, "exception.user.OldPasswordIsWrong"),
    PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "exception.user.PasswordsDoNotMatch"),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "exception.user.EmailAlreadyExists"),
    NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "exception.global.NameAlreadyExists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.user.NotFound"),
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.program.NotFound"),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.event.NotFound"),
    OFFERING_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.offering.NotFound"),
    OFFERING_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "exception.offering.NotAvailable"),
    FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.folder.NotFound"),
    FOLDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.folderItem.NotFound"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.order.NotFound"),
    SIGNATURES_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "exception.payment.SignaturesDoNotMatch"),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.article.NotFound");

    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;

}
