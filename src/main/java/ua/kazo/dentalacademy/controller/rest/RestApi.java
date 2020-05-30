package ua.kazo.dentalacademy.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.kazo.dentalacademy.config.CustomLiqPay;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;
import ua.kazo.dentalacademy.service.OrderService;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestApi {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final CustomLiqPay liqPay;

    @GetMapping("/order/{id}/status")
    public LiqPayPaymentStatus orderGet(@PathVariable final String id) {
        return orderService.findByNumber(id).getStatus();
    }

    @PostMapping("/liqpay-callback")
    public void liqPayCallback(final String data, final String signature) throws JsonProcessingException {
        String appSignature = liqPay.createSignature(data);
        if (!appSignature.equals(signature)) {
            throw new RuntimeException("Signatures do not match");
        }
        String decodedJsonString = new String(DatatypeConverter.parseBase64Binary(data));
        Map<String, String> dataMap = objectMapper.readValue(decodedJsonString, new TypeReference<>() {});
        String orderNumber = dataMap.get("order_id");
        String orderStatus = dataMap.get("status");
        orderService.setStatus(orderNumber, orderStatus);
    }

}
