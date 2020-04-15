package ua.kazo.dentalacademy.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ExceptionCode {

    FOLDER_ITEM_TYPE_NOT_EXIST(HttpStatus.NOT_FOUND, "Folder item type {0} not exist"),
    FOLDER_CATEGORY_NOT_EXIST(HttpStatus.NOT_FOUND, "Folder category {0} not exist"),
    USER_ROLE_NOT_EXIST(HttpStatus.NOT_FOUND, "User role {0} not exist"),
    INSUFFICIENT_PRIVILEGES(HttpStatus.FORBIDDEN, "Insufficient privileges"),
    FOLDER_IS_EMPTY(HttpStatus.I_AM_A_TEAPOT, "Folder {0} is empty"),
    OLD_PASSWORD_IS_WRONG(HttpStatus.BAD_REQUEST, "Old password is wrong"),
    PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "Passwords do not match"),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Email {0} already exists"),
    NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Name {0} already exists"),
    OFFERING_ALREADY_BOUGHT(HttpStatus.BAD_REQUEST, "Offering {0} already bought by user {1}"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User {0} not found"),
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "Program {0} not found"),
    OFFERING_NOT_FOUND(HttpStatus.NOT_FOUND, "Offering {0} not found"),
    FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Folder not found"),
    FOLDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Folder item {0} not found"),
    FOLDER_CATEGORY_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "Folder category '{0}' does not exist");

    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;

}
