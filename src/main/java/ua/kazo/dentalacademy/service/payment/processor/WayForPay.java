package ua.kazo.dentalacademy.service.payment.processor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.properties.AppProperties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WayForPay implements PaymentProcessor {

    private static final String WAYFORPAY_API_CHECKOUT_URL = "https://secure.wayforpay.com/pay";
    private static final String ALGORITHM = "HmacMD5";
    private static final String DELIMITER = ";";
    private static final String SIGNATURE_KEY = "merchantSignature";

    private final AppProperties appProperties;

    @Override
    public String getCheckoutUrl() {
        return WAYFORPAY_API_CHECKOUT_URL;
    }

    @Override
    public Map<String, Object> api(String url, Map<String, String> parameters) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Map<String, Object> getPayParameters(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("merchantAccount", appProperties.getPayment().getWayforpay().getMerchantLogin());
        params.put("merchantDomainName", appProperties.getPayment().getCallbackHost());
        params.put("merchantTransactionSecureType", "AUTO");
        params.put("language", LocaleContextHolder.getLocale().getLanguage());
        params.put("returnUrl", appProperties.getPayment().getCallbackHost() + "/order/" + order.getNumber());
        params.put("serviceUrl", appProperties.getPayment().getCallbackHost() + "/api/payment/wayforpay-callback");
        params.put("orderReference", order.getNumber());
        params.put("orderDate", String.valueOf(order.getCreated().atZone(ZoneId.systemDefault()).toEpochSecond()));
        params.put("amount", order.getPrice().toString());
        params.put("currency", appProperties.getPayment().getCurrency());
        params.put("orderTimeout", String.valueOf(appProperties.getPayment().getPaymentTime() * 60));
        List<String> productName = new ArrayList<>(order.getPurchaseData().size());
        List<BigDecimal> productPrice = new ArrayList<>(order.getPurchaseData().size());
        List<Integer> productCount = new ArrayList<>(order.getPurchaseData().size());
        order.getPurchaseData().forEach(purchaseData -> {
            productName.add(purchaseData.getName());
            productPrice.add(purchaseData.getPrice());
            productCount.add(1);
        });
        params.put("productName", productName);
        params.put("productPrice", productPrice);
        params.put("productCount", productCount);
        params.put("clientEmail", SecurityContextHolder.getContext().getAuthentication().getName());
        params.put(SIGNATURE_KEY, generateSignature(params, false));
        return params;
    }

    private String getPurchaseRequestSignatureString(Map<String, Object> params) {
        StringJoiner joiner = new StringJoiner(";")
                .add(params.get("merchantAccount").toString())
                .add(params.get("merchantDomainName").toString())
                .add(params.get("orderReference").toString())
                .add(params.get("orderDate").toString())
                .add(params.get("amount").toString())
                .add(params.get("currency").toString());
        ((List<?>) params.get("productName")).forEach(item -> joiner.add(item.toString()));
        ((List<?>) params.get("productCount")).forEach(item -> joiner.add(item.toString()));
        ((List<?>) params.get("productPrice")).forEach(item -> joiner.add(item.toString()));
        return joiner.toString();
    }

    private String getPurchaseResponseSignatureString(Map<String, Object> params) {
        StringJoiner joiner = new StringJoiner(DELIMITER)
                .add(params.get("merchantAccount").toString())
                .add(params.get("orderReference").toString())
                .add(params.get("amount").toString())
                .add(params.get("currency").toString())
                .add(params.get("authCode").toString())
                .add(params.get("cardPan").toString())
                .add(params.get("transactionStatus").toString())
                .add(params.get("reasonCode").toString());
        return joiner.toString();
    }

    @SneakyThrows
    private String generateSignature(Map<String, Object> params, boolean isResponse)  {
        String keyString = appProperties.getPayment().getWayforpay().getMerchantSecretKey();
        String baseString = isResponse ? getPurchaseResponseSignatureString(params) : getPurchaseRequestSignatureString(params);
        SecretKeySpec secret = new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(secret);
        byte[] digest = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(digest);
    }

    @Override
    public Map<String, String> getReceiptParameters(Order order, String userEmail) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String getParamsAsString(Order order) {
        return getPayParameters(order).toString();
    }

    public boolean isSignaturesEquals(Map<String, Object> params) {
        Object signature1 = params.get(SIGNATURE_KEY);
        String signature2 = generateSignature(params, true);
        return signature1.equals(signature2);
    }

}
