package ua.kazo.dentalacademy.service.payment.processor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.properties.AppProperties;

import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class Fondy implements PaymentProcessor {

    private static final String FONDY_API_CHECKOUT_URL = "https://api.fondy.eu/api/checkout/redirect/";
    private static final String DELIMITER = "|";
    private static final String SIGNATURE_KEY = "signature";
    private static final String RESPONSE_SIGNATURE_STRING_KEY = "response_signature_string";

    private final AppProperties appProperties;

    @Override
    public String getCheckoutUrl() {
        return FONDY_API_CHECKOUT_URL;
    }

    @Override
    public Map<String, Object> api(String url, Map<String, String> parameters) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Map<String, Object> getPayParameters(Order order) {
        Map<String, Object> params = new TreeMap<>();
        params.put("amount", order.getPrice().toString().replace(".", ""));
        params.put("currency", appProperties.getPayment().getCurrency());
        params.put("lang", LocaleContextHolder.getLocale().getLanguage());
        params.put("lifetime", String.valueOf(appProperties.getPayment().getPaymentTime() * 60));
        params.put("merchant_id", appProperties.getPayment().getFondy().getMerchantId());
        params.put("order_desc", "Buying courses at Dental Academy");
        params.put("order_id", order.getNumber());
        params.put("response_url", appProperties.getPayment().getCallbackHost() + "/order/" + order.getNumber());
        params.put("sender_email", SecurityContextHolder.getContext().getAuthentication().getName());
        params.put("server_callback_url", appProperties.getPayment().getCallbackHost() + "/api/payment/fondy-callback");
        params.put("version", "1.0.1");
        params.put(SIGNATURE_KEY, generateSignature(params));
        return params;
    }

    private String generateSignature(Map<String, Object> parameters) {
        StringJoiner signature = new StringJoiner(DELIMITER);
        signature.add(appProperties.getPayment().getFondy().getMerchantPassword());
        parameters.forEach((key, value) -> {
            if (ObjectUtils.isEmpty(value) || SIGNATURE_KEY.equals(key) || RESPONSE_SIGNATURE_STRING_KEY.equals(key)) {
                return;
            }
            signature.add(value.toString());
        });
        return DigestUtils.sha1Hex(signature.toString());
    }

    public boolean isSignaturesEquals(Map<String, String> parameters) {
        String signature1 = parameters.get(SIGNATURE_KEY);
        String signature2 = generateSignature(new TreeMap<>(parameters));
        return signature1.equals(signature2);
    }

    @Override
    public Map<String, String> getReceiptParameters(Order order, String userEmail) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String getParamsAsString(Order order) {
        return getPayParameters(order).toString();
    }

}
