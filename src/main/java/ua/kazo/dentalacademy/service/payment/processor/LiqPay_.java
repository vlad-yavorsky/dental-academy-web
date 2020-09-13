package ua.kazo.dentalacademy.service.payment.processor;

import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import org.json.simple.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.config.payment.PaymentProperties;
import ua.kazo.dentalacademy.entity.Order;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LiqPay_ extends LiqPay implements PaymentProcessor {

    private final PaymentProperties paymentProperties;

    public LiqPay_(PaymentProperties paymentProperties) {
        super(paymentProperties.getLiqpay().getPublicKey(), paymentProperties.getLiqpay().getPrivateKey());
        this.paymentProperties = paymentProperties;
    }

    private String convertToJsonAndEncodeToBase64(Map<String, String> params) {
        return LiqPayUtil.base64_encode(JSONObject.toJSONString(withBasicApiParams(params)));
    }

    public boolean isSignaturesEquals(String data, String signature) {
        String ourSignature = createSignature(data);
        return ourSignature.equals(signature);
    }

    @Override
    public String getCheckoutUrl() {
        return LiqPay.LIQPAY_API_CHECKOUT_URL;
    }

    @Override
    public Map<String, String> getPayParameters(Order order) {
        String data = convertToJsonAndEncodeToBase64(payParamsInternal(order));
        String signature = createSignature(data);
        Map<String, String> params = new HashMap<>();
        params.put("data", data);
        params.put("signature", signature);
        return params;
    }

    private Map<String, String> payParamsInternal(Order order) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", order.getPrice().toString());
        params.put("currency", paymentProperties.getCurrency());
        params.put("description", "Buying courses at Dental Academy");
        params.put("expired_date", order.getCreated().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .plusMinutes(paymentProperties.getPaymentTime()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("language", LocaleContextHolder.getLocale().getLanguage());
        params.put("order_id", order.getNumber());
        params.put("result_url", paymentProperties.getCallbackHost() + "/order/" + order.getNumber());
        params.put("server_url", paymentProperties.getCallbackHost() + "/api/payment/liqpay-callback");
        return params;
    }

    @Override
    public Map<String, String> getReceiptParameters(Order order, String userEmail) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "ticket");
        params.put("email", userEmail);
        params.put("order_id", order.getNumber());
        params.put("language", LocaleContextHolder.getLocale().getLanguage());
        params.put("stamp", "true");
        return params;
    }

    @Override
    public String getParamsAsString(Order order) {
        return JSONObject.toJSONString(withBasicApiParams(payParamsInternal(order)));
    }

}
