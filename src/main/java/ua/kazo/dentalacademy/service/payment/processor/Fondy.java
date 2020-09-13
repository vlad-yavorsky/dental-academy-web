package ua.kazo.dentalacademy.service.payment.processor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ua.kazo.dentalacademy.config.payment.PaymentProperties;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.util.LogUtils;

import java.util.Map;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class Fondy implements PaymentProcessor {

    private static final String FONDY_API_CHECKOUT_URL = "https://api.fondy.eu/api/checkout/redirect/";
    private static final String SEPARATOR = "|";
    private static final String SIGNATURE = "signature";
    private static final String RESPONSE_SIGNATURE_STRING = "response_signature_string";

    private final PaymentProperties paymentProperties;

    @Override
    public String getCheckoutUrl() {
        return FONDY_API_CHECKOUT_URL;
    }

    @Override
    public Map<String, Object> api(String url, Map<String, String> parameters) {
        throw new RuntimeException("Not implemented"); // todo: implement
    }

    @Override
    public Map<String, String> getPayParameters(Order order) {
        Map<String, String> params = new TreeMap<>();
        params.put("amount", order.getPrice().toString().replace(".", ""));
        params.put("currency", paymentProperties.getCurrency());
        params.put("lang", LocaleContextHolder.getLocale().getLanguage());
        params.put("lifetime", String.valueOf(paymentProperties.getPaymentTime() * 60));
        params.put("merchant_id", paymentProperties.getFondy().getMerchantId());
        params.put("order_desc", "Buying courses at Dental Academy");
        params.put("order_id", order.getNumber());
        params.put("response_url", paymentProperties.getCallbackHost() + "/order/" + order.getNumber());
        params.put("sender_email", SecurityContextHolder.getContext().getAuthentication().getName());
        params.put("server_callback_url", paymentProperties.getCallbackHost() + "/api/payment/fondy-callback");
        params.put("version", "1.0.1");
        params.put("signature", generateSignature(params));
        return params;
    }

    private String generateSignature(Map<String, String> parameters) {
        StringBuilder signature = new StringBuilder(paymentProperties.getFondy().getMerchantPassword());
        parameters.forEach((key, value) -> {
            if (StringUtils.isEmpty(value) || SIGNATURE.equals(key) || RESPONSE_SIGNATURE_STRING.equals(key)) {
                return;
            }
            signature.append(SEPARATOR).append(value);
        });
        return DigestUtils.sha1Hex(signature.toString());
    }

    public boolean isSignaturesEquals(Map<String, String> parameters) {
        String signature1 = parameters.get(SIGNATURE);
        String signature2 = generateSignature(new TreeMap<>(parameters));
        return signature1.equals(signature2);
    }

    @Override
    public Map<String, String> getReceiptParameters(Order order, String userEmail) {
        throw new RuntimeException("Not implemented"); // todo: implement
    }

    @Override
    public String getParamsAsString(Order order) {
        return LogUtils.getParamsAsString(getPayParameters(order));
    }

}
