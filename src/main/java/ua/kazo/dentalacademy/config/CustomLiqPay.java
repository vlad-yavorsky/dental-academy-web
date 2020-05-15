package ua.kazo.dentalacademy.config;

import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import org.json.simple.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CustomLiqPay extends LiqPay {

    public CustomLiqPay(String publicKey, String privateKey) {
        super(publicKey, privateKey);
    }

    public String convertToJsonAndEncodeToBase64(Map<String, String> params) {
        return LiqPayUtil.base64_encode(JSONObject.toJSONString(withBasicApiParams(params)));
    }

    public String createSignature(String base64EncodedData) {
        return super.createSignature(base64EncodedData);
    }

    public Map<String, String> createParams(BigDecimal price, String description, Long orderId, String host) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", price.toString());
        params.put("currency", "UAH");
        params.put("description", description);
        params.put("language", LocaleContextHolder.getLocale().getLanguage());
        params.put("order_id", orderId.toString());
        params.put("result_url", host + "/order/" + orderId);
        params.put("server_url", host + "/api/liqpay-callback");
        return params;
    }

}
