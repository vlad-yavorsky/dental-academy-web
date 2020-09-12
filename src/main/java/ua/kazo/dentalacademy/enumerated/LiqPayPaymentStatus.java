package ua.kazo.dentalacademy.enumerated;

import lombok.Getter;

import java.util.Arrays;

public enum LiqPayPaymentStatus {
    // Custom
    CREATED,

    // Final payment statuses
    ERROR,
    FAILURE,
    REVERSED,
    SUBSCRIBED,
    SUCCESS,
    UNSUBSCRIBED,

    // Statuses that required payment confirmation
    _3DS_VERIFY("3ds_verify"),
    CAPTCHA_VERIFY,
    CVV_VERIFY,
    IVR_VERIFY,
    OTP_VERIFY,
    PASSWORD_VERIFY,
    PHONE_VERIFY,
    PIN_VERIFY,
    RECEIVER_VERIFY,
    SENDER_VERIFY,
    SENDERAPP_VERIFY,
    WAIT_QR,
    WAIT_SENDER,

    // Other payment statuses
    CASH_WAIT,
    HOLD_WAIT,
    INVOICE_WAIT,
    PREPARED,
    PROCESSING,
    WAIT_ACCEPT,
    WAIT_CARD,
    WAIT_COMPENSATION,
    WAIT_LC,
    WAIT_RESERVE,
    WAIT_SECURE;

    LiqPayPaymentStatus() {
        this.code = name();
    }

    LiqPayPaymentStatus(String code) {
        this.code = code;
    }

    @Getter
    private final String code;

    public static LiqPayPaymentStatus of(String code) {
        return Arrays.stream(LiqPayPaymentStatus.values())
                .filter(status -> status.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant for code " + code));
    }

}
