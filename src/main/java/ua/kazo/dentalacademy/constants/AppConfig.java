package ua.kazo.dentalacademy.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AppConfig {

    DEFAULT_PAGE_SIZE(Constants.DEFAULT_PAGE_SIZE_VALUE);

    @Getter
    private final String value;

    public static final class Constants {
        private Constants() {}
        public static final String DEFAULT_PAGE_SIZE_VALUE = "15";
    }

}
