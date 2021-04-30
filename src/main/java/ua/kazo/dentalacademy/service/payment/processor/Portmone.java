package ua.kazo.dentalacademy.service.payment.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.properties.AppProperties;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Portmone implements PaymentProcessor {

    public static final String PORTMONE_API_GATEWAY_URL = "https://www.portmone.com.ua/gateway/";

    private final AppProperties appProperties;

    @Override
    public String getCheckoutUrl() {
        return PORTMONE_API_GATEWAY_URL;
    }

    @Override
    public Map<String, Object> api(String url, Map<String, String> parameters) {
        throw new RuntimeException("Not implemented"); // todo: implement
    }

    @Override
    public Map<String, Object> getPayParameters(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("payee_id", appProperties.getPayment().getPortmone().getPayeeId());
        params.put("shop_order_number", order.getNumber());
        params.put("bill_amount", order.getPrice().toString());
        params.put("bill_currency", appProperties.getPayment().getCurrency());
        params.put("description", "Buying courses at Dental Academy");
        params.put("success_url", appProperties.getPayment().getCallbackHost() + "/api/payment/portmone-success");
        params.put("failure_url", appProperties.getPayment().getCallbackHost() + "/api/payment/portmone-failure");
        params.put("encoding", "UTF-8");
        params.put("exp_time", String.valueOf(appProperties.getPayment().getPaymentTime() * 60));
        params.put("lang", LocaleContextHolder.getLocale().getLanguage());
        params.put("email_address", SecurityContextHolder.getContext().getAuthentication().getName());
        return params;
    }

    @Override
    public Map<String, String> getReceiptParameters(Order order, String userEmail) {
        throw new RuntimeException("Not implemented"); // todo: implement
    }

    @Override
    public String getParamsAsString(Order order) {
        return getPayParameters(order).toString();
    }

}
