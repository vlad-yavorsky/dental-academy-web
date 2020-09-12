package ua.kazo.dentalacademy.config;

import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import org.json.simple.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.entity.Order;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomLiqPay extends LiqPay {

    private final LiqPayProperties liqPayProperties;

    public CustomLiqPay(LiqPayProperties liqPayProperties) {
        super(liqPayProperties.getPublicKey(), liqPayProperties.getPrivateKey());
        this.liqPayProperties = liqPayProperties;
    }

    public String convertToJsonAndEncodeToBase64(Map<String, String> params) {
        return LiqPayUtil.base64_encode(JSONObject.toJSONString(withBasicApiParams(params)));
    }

    public String createSignature(String base64EncodedData) {
        return super.createSignature(base64EncodedData);
    }

    public String createParamsAndConvertToJson(Order order) {
        return JSONObject.toJSONString(withBasicApiParams(payParams(order)));
    }

    public Map<String, String> payParams(Order order) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", order.getPrice().toString());
        params.put("currency", "UAH");
        params.put("description", "");
        params.put("expired_date", order.getCreated().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .plusMinutes(liqPayProperties.getPaymentTime()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("language", LocaleContextHolder.getLocale().getLanguage());
        params.put("order_id", order.getNumber());
        params.put("result_url", liqPayProperties.getCallbackHost() + "/order/" + order.getNumber());
        params.put("server_url", liqPayProperties.getCallbackHost() + "/api/liqpay-callback");
        return params;
    }

    public Map<String, String> receiptParams(String orderNumber, String userEmail) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "ticket");
        params.put("email", userEmail);
        params.put("order_id", orderNumber);
        params.put("language", LocaleContextHolder.getLocale().getLanguage());
        params.put("stamp", "true");
        return params;
    }

}
