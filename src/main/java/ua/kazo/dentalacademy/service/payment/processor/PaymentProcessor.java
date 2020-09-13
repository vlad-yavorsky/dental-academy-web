package ua.kazo.dentalacademy.service.payment.processor;

import ua.kazo.dentalacademy.entity.Order;

import java.util.Map;

public interface PaymentProcessor {

    String getCheckoutUrl();

    Map<String, Object> api(String url, Map<String, String> parameters) throws Exception;

    Map<String, String> getPayParameters(Order order);

    Map<String, String> getReceiptParameters(Order order, String userEmail);

    String getParamsAsString(Order order);

}
