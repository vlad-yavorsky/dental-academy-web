package ua.kazo.dentalacademy.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.service.OrderService;
import ua.kazo.dentalacademy.service.payment.convertor.PortmonePaymentStatus;
import ua.kazo.dentalacademy.service.payment.processor.Fondy;
import ua.kazo.dentalacademy.service.payment.processor.LiqPay;
import ua.kazo.dentalacademy.service.payment.processor.PaymentProcessorHolder;
import ua.kazo.dentalacademy.service.payment.processor.WayForPay;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RestApi {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    /**
     * Refresh order status via ajax request
     *
     * @param id order ID
     * @return LiqPay payment status
     */
    @GetMapping("/order/{id}/status")
    public UnifiedPaymentStatus orderGet(@PathVariable final String id) {
        return orderService.findByNumber(id).getStatus();
    }

    /**
     * Getting status and payment information from LiqPay payment system
     *
     * @param data      json string with APIs parameters encoded with base64 function, base64_encode(json_string),
     * @param signature unique signature of each request, base64_encode(sha1(private_key + data + private_key))
     * @throws JsonProcessingException if problems encountered when processing JSON content
     */
    @SneakyThrows
    @PostMapping("/payment/liqpay-callback")
    public void liqPayCallback(final String data, final String signature) {
        log.debug("LiqPay Data: {}; Signature: {}", data, signature);
        LiqPay liqPay = (LiqPay) PaymentProcessorHolder.get(PaymentProvider.LIQPAY);
        if (!liqPay.isSignaturesEquals(data, signature)) {
            throw new ApplicationException(messageSource, ExceptionCode.SIGNATURES_DO_NOT_MATCH);
        }
        String decodedJsonString = new String(DatatypeConverter.parseBase64Binary(data));
        log.debug("LiqPay Decoded Data: " + decodedJsonString);
        Map<String, Object> dataMap = objectMapper.readValue(decodedJsonString, new TypeReference<>() {});
        String orderNumber = dataMap.get("order_id").toString();
        String orderStatus = dataMap.get("status").toString();
        orderService.updateOrder(orderNumber, orderStatus, decodedJsonString);
    }

    @PostMapping("/payment/portmone-success")
    public void portmoneSuccessUrl(@RequestParam Map<String, String> response) {
        log.info("Portmone Data: {}", response);
    }

    @PostMapping("/payment/portmone-failure")
    public void portmoneFailureUrl(@RequestParam Map<String, String> response) {
        log.info("Portmone Data: {}", response);
    }

    @PostMapping("/payment/fondy-callback")
    public void fondyCallback(@RequestParam Map<String, String> response) {
        log.info("Fondy Data: {}", response);
        Fondy fondy = (Fondy) PaymentProcessorHolder.get(PaymentProvider.FONDY);
        if (!fondy.isSignaturesEquals(response)) {
            throw new ApplicationException(messageSource, ExceptionCode.SIGNATURES_DO_NOT_MATCH);
        }
        String data = response.toString();
        String orderNumber = response.get("order_id");
        String orderStatus = response.get("order_status");
        orderService.updateOrder(orderNumber, orderStatus, data);
    }

    @SneakyThrows
    @PostMapping(value = "/payment/wayforpay-callback")
    public void wayForPayCallback(@RequestParam Map<String, String> response) {
        String json = response.entrySet().iterator().next().getKey(); // Workaround for bad input parameters
        Map<String, Object> responseMap = objectMapper.readValue(json, new TypeReference<>() {});
        String data = responseMap.toString();
        log.info("WayForPay Data: {}", data);
        WayForPay wayForPay = (WayForPay) PaymentProcessorHolder.get(PaymentProvider.WAYFORPAY);
        if (!wayForPay.isSignaturesEquals(responseMap)) {
            throw new ApplicationException(messageSource, ExceptionCode.SIGNATURES_DO_NOT_MATCH);
        }
        String orderNumber = (String) responseMap.get("orderReference");
        String orderStatus = (String) responseMap.get("transactionStatus");
        orderService.updateOrder(orderNumber, orderStatus, data);
    }

}
