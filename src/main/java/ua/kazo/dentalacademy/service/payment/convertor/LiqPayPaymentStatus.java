package ua.kazo.dentalacademy.service.payment.convertor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

@Component
public class LiqPayPaymentStatus extends PaymentStatusConverter {

    // Final payment statuses
    public static final String ERROR = "error";
    public static final String FAILURE = "failure";
    public static final String REVERSED = "reversed";
    public static final String SUBSCRIBED = "subscribed";
    public static final String SUCCESS = "success";
    public static final String UNSUBSCRIBED = "unsubscribed";

    // Statuses that required payment confirmation
    public static final String VERIFY_3DS = "3ds_verify";
    public static final String CAPTCHA_VERIFY = "captcha_verify";
    public static final String CVV_VERIFY = "cvv_verify";
    public static final String IVR_VERIFY = "ivr_verify";
    public static final String OTP_VERIFY = "otp_verify";
    public static final String PASSWORD_VERIFY = "password_verify";
    public static final String PHONE_VERIFY = "phone_verify";
    public static final String PIN_VERIFY = "pin_verify";
    public static final String RECEIVER_VERIFY = "receiver_verify";
    public static final String SENDER_VERIFY = "sender_verify";
    public static final String SENDERAPP_VERIFY = "senderapp_verify";
    public static final String WAIT_QR = "wait_qr";
    public static final String WAIT_SENDER = "wait_sender";

    // Other payment statuses
    public static final String CASH_WAIT = "cash_wait";
    public static final String HOLD_WAIT = "hold_wait";
    public static final String INVOICE_WAIT = "invoice_wait";
    public static final String PREPARED = "prepared";
    public static final String PROCESSING = "processing";
    public static final String WAIT_ACCEPT = "wait_accept";
    public static final String WAIT_CARD = "wait_card";
    public static final String WAIT_COMPENSATION = "wait_compensation";
    public static final String WAIT_LC = "wait_lc";
    public static final String WAIT_RESERVE = "wait_reserve";
    public static final String WAIT_SECURE = "wait_secure";

    public LiqPayPaymentStatus() {
        map.put(LiqPayPaymentStatus.ERROR, UnifiedPaymentStatus.FAILURE);
        map.put(LiqPayPaymentStatus.FAILURE, UnifiedPaymentStatus.FAILURE);
        map.put(LiqPayPaymentStatus.REVERSED, UnifiedPaymentStatus.REVERSED);
        map.put(LiqPayPaymentStatus.SUCCESS, UnifiedPaymentStatus.SUCCESS);
    }

}
