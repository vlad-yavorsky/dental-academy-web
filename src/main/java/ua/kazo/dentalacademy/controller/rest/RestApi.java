package ua.kazo.dentalacademy.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.kazo.dentalacademy.config.CustomLiqPay;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;
import ua.kazo.dentalacademy.service.OrderService;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RestApi {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final CustomLiqPay liqPay;

    /**
     * Refresh order status via ajax request
     *
     * @param id order ID
     * @return LiqPay payment status
     */
    @GetMapping("/order/{id}/status")
    public LiqPayPaymentStatus orderGet(@PathVariable final String id) {
        return orderService.findByNumber(id).getStatus();
    }

    /**
     * Getting status and payment information from LiqPay payment system
     *
     * @param data      json string with APIs parameters encoded with base64 function, base64_encode(json_string),
     * @param signature unique signature of each request, base64_encode(sha1(private_key + data + private_key))
     * @throws JsonProcessingException if problems encountered when processing JSON content
     */
    @PostMapping("/liqpay-callback")
    public void liqPayCallback(final String data, final String signature) throws JsonProcessingException {
        String appSignature = liqPay.createSignature(data);
        if (!appSignature.equals(signature)) {
            throw new RuntimeException("Signatures do not match");
        }
        String decodedJsonString = new String(DatatypeConverter.parseBase64Binary(data));
        Map<String, String> dataMap = objectMapper.readValue(decodedJsonString, new TypeReference<>() {
        });
        log.debug("LiqPay Callback: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataMap));
        String orderNumber = dataMap.get("order_id");
        String orderStatus = dataMap.get("status");
        orderService.changeStatus(orderNumber, orderStatus);
    }

}
