package ua.kazo.dentalacademy.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;
import ua.kazo.dentalacademy.mapper.OrderMapper;
import ua.kazo.dentalacademy.service.OrderService;
import ua.kazo.dentalacademy.service.payment.OrderManualUpdateLogItem;
import ua.kazo.dentalacademy.service.payment.processor.PaymentProcessor;
import ua.kazo.dentalacademy.service.payment.processor.PaymentProcessorHolder;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/orders")
    public String orders(final ModelMap model,
                        @RequestParam(defaultValue = "0") final int page,
                        @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Order> pageResult = orderService.findAllFetchUserRoles(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.ORDERS, orderMapper.toUserResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "admin/order/orders";
    }

    @GetMapping("/order/{orderNumber}")
    public String findOrder(@PathVariable final String orderNumber, final ModelMap model) {
        Order order = orderService.findByNumberFetchCompletePurchaseData(orderNumber, true);
        PaymentProcessor paymentProcessor = PaymentProcessorHolder.get(order.getProvider());
        model.addAttribute("checkoutUrl", paymentProcessor.getCheckoutUrl());
        model.addAttribute("formData", paymentProcessor.getPayParameters(order));
        model.addAttribute("order", orderMapper.toUserPurchaseDataResponseDto(order));
        return "admin/order/order";
    }

    @PostMapping("/order/{orderNumber}/status")
    public String manualUpdateOrderStatus(@PathVariable final String orderNumber,
                                          @RequestParam("orderStatus") final UnifiedPaymentStatus orderStatus,
                                          final Principal principal) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        orderService.manualUpdateOrder(orderNumber, orderStatus,
                mapper.writeValueAsString(new OrderManualUpdateLogItem("manual_update", principal.getName(), orderStatus)));
        return "redirect:/admin/order/" + orderNumber;
    }

}