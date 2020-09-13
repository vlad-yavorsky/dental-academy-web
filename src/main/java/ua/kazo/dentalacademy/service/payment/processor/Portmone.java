package ua.kazo.dentalacademy.service.payment.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.config.payment.PaymentProperties;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Portmone implements PaymentProcessor {

    public static final String PORTMONE_API_GATEWAY_URL = "https://www.portmone.com.ua/gateway/";

    private final PaymentProperties paymentProperties;

    @Override
    public String getCheckoutUrl() {
        return PORTMONE_API_GATEWAY_URL;
    }

    @Override
    public Map<String, Object> api(String url, Map<String, String> parameters) {
        throw new RuntimeException("Not implemented"); // todo: implement
    }

    @Override
    public Map<String, String> getPayParameters(Order order) {
        Map<String, String> params = new HashMap<>();
        params.put("payee_id", paymentProperties.getPortmone().getPayeeId());
        params.put("shop_order_number", order.getNumber());
        params.put("bill_amount", order.getPrice().toString());
        params.put("bill_currency", paymentProperties.getCurrency());
        params.put("description", "Buying courses at Dental Academy");
        params.put("success_url", paymentProperties.getCallbackHost() + "/api/payment/portmone-success");
        params.put("failure_url", paymentProperties.getCallbackHost() + "/api/payment/portmone-failure");
        params.put("encoding", "UTF-8");
        params.put("exp_time", String.valueOf(paymentProperties.getPaymentTime() * 60));
        params.put("lang", LocaleContextHolder.getLocale().getLanguage());
        return params;
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
