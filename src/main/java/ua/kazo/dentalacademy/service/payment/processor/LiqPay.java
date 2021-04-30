package ua.kazo.dentalacademy.service.payment.processor;

import com.liqpay.LiqPayUtil;
import org.json.simple.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.properties.AppProperties;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LiqPay extends com.liqpay.LiqPay implements PaymentProcessor {

    private final AppProperties appProperties;

    public LiqPay(AppProperties appProperties) {
        super(appProperties.getPayment().getLiqpay().getPublicKey(), appProperties.getPayment().getLiqpay().getPrivateKey());
        this.appProperties = appProperties;
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
    public Map<String, Object> getPayParameters(Order order) {
        String data = convertToJsonAndEncodeToBase64(payParamsInternal(order));
        String signature = createSignature(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", data);
        params.put("signature", signature);
        return params;
    }

    private Map<String, String> payParamsInternal(Order order) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", order.getPrice().toString());
        params.put("currency", appProperties.getPayment().getCurrency());
        params.put("description", "Buying courses at Dental Academy");
        params.put("expired_date", order.getCreated().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .plusMinutes(appProperties.getPayment().getPaymentTime()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("language", LocaleContextHolder.getLocale().getLanguage());
        params.put("order_id", order.getNumber());
        params.put("result_url", appProperties.getPayment().getCallbackHost() + "/order/" + order.getNumber());
        params.put("server_url", appProperties.getPayment().getCallbackHost() + "/api/payment/liqpay-callback");
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
