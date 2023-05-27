package ua.kazo.dentalacademy.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Languages {
    EN("us", "en"),
    UK("ua", "uk");

    @Getter
    private final String country;

    @Getter
    private final String language;

}
