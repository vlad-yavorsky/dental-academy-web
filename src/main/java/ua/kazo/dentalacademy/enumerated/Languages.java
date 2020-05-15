package ua.kazo.dentalacademy.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Languages {
    EN("us", "en"),
    UK("ua", "uk"),
    RU("ru", "ru");

    @Getter
    private final String country;

    @Getter
    private final String language;

}
