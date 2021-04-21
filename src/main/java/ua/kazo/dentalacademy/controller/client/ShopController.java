package ua.kazo.dentalacademy.controller.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.offering.ShopItemOfferingResponseDto;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.mapper.OfferingMapper;
import ua.kazo.dentalacademy.mapper.OrderMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.mapper.UserMapper;
import ua.kazo.dentalacademy.security.Permission;
import ua.kazo.dentalacademy.security.TargetType;
import ua.kazo.dentalacademy.service.*;
import ua.kazo.dentalacademy.service.payment.processor.PaymentProcessor;
import ua.kazo.dentalacademy.service.payment.processor.PaymentProcessorHolder;
import ua.kazo.dentalacademy.util.AuthUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final OfferingService offeringService;
    private final OfferingMapper offeringMapper;
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final PurchaseDataService purchaseDataService;
    private final UserService userService;
    private final UserMapper userMapper;

    /* ---------------------------------------------- SHOP ---------------------------------------------- */

    @GetMapping("/shop")
    public String getShop(final ModelMap model, @RequestParam(required = false) final String search,
                          @RequestParam(defaultValue = "0") final int page,
                          @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllByNotDeactivatedOfferings(search, PageRequest.of(page, size));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toOfferingsResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "client/shop/shop";
    }

    /* ---------------------------------------------- SHOP ITEM ---------------------------------------------- */

    @GetMapping("/shop/program/{programId}")
    public String getShopItem(final ModelMap model, @PathVariable final Long programId, final Principal principal) {
        List<Long> offeringIds = offeringService.findAllIdsByProgramId(programId);
        model.addAttribute(ModelMapConstants.PROGRAM, programMapper.toResponseDto(programService.findById(programId)));
        List<ShopItemOfferingResponseDto> shopItemOfferingDtos = offeringMapper.toShopItemResponseDto(
                offeringService.findAllByIdsAndNotDeactivatedFetchProgramsAndFolders(offeringIds),
                userService.findByEmailFetchCartItemsAndOrders(principal.getName(),
                        () -> orderService.findAllByUserEmail(principal.getName())));
        model.addAttribute(ModelMapConstants.OFFERINGS, shopItemOfferingDtos);
        model.addAttribute(ModelMapConstants.IS_PURCHASED, purchaseDataService.isProgramPurchasedAndNotExpired(programId, principal.getName()));
        model.addAttribute(ModelMapConstants.NOW, LocalDateTime.now());
        return "client/shop/shop-item";
    }

    /* ---------------------------------------------- ORDER ---------------------------------------------- */

    @GetMapping("/create-order")
    public RedirectView createOrder(final Principal principal) {
        Order order = orderService.create(principal.getName());
        return new RedirectView("/order/" + order.getNumber());
    }

    @PreAuthorize("hasPermission(#orderNumber, '" + TargetType.ORDER + "', '" + Permission.READ + "')")
    @GetMapping("/order/{orderNumber}")
    public String findOrder(@PathVariable final String orderNumber, final ModelMap model) {
        Order order = orderService.findByNumberFetchCompletePurchaseData(orderNumber, false);
        PaymentProcessor paymentProcessor = PaymentProcessorHolder.get(order.getProvider());
        model.addAttribute("checkoutUrl", paymentProcessor.getCheckoutUrl());
        model.addAttribute("formData", paymentProcessor.getPayParameters(order));
        model.addAttribute("order", orderMapper.toPurchaseDataResponseDto(order));
        return "client/shop/order";
    }

    @PreAuthorize("hasPermission(#orderNumber, '" + TargetType.ORDER + "', '" + Permission.READ + "')")
    @GetMapping("/order/{orderNumber}/receipt")
    public String sendOrderReceipt(@PathVariable final String orderNumber, final ModelMap model, final Principal principal) throws JsonProcessingException {
        Order order = orderService.findByNumber(orderNumber);
        PaymentProcessor paymentProcessor = PaymentProcessorHolder.get(order.getProvider());
        String userEmail = principal.getName();
        Map<String, String> receiptParams = paymentProcessor.getReceiptParameters(order, userEmail);
        try {
            Map<String, Object> response = paymentProcessor.api("request", receiptParams);
            if (response.get("result").equals("ok")) {
                log.info("Receipt of order {} successfully sent to user {}", orderNumber, userEmail);
            } else if (response.get("result").equals("error")) {
                log.info("Receipt of order {} did not send to user {}. Code: {}. Description: {}",
                        orderNumber, userEmail, response.get("err_code"), response.get("err_description"));
            }
        } catch (Exception e) {
            log.error("Can't send request to LiqPay server: ", e);
        }
        return findOrder(orderNumber, model);
    }

    /* ---------------------------------------------- CART ---------------------------------------------- */

    @GetMapping("/cart")
    public String getCart(final ModelMap model, final Principal principal) {
        User user = userService.findByEmailFetchCartItemsAndProgramsAndBonuses(principal.getName());
        model.addAttribute("userCart", userMapper.toCartDto(user));
        AuthUtils.updateCartItemsCount(principal, user.getCartItemsCount());
        return "client/shop/cart";
    }

    @PostMapping(value = "/cart", params = {"addItem"})
    public String addItemToCart(final @RequestParam Long offeringId, final HttpServletRequest request, final Principal principal) {
        User user = userService.addItemToCart(principal.getName(), offeringId);
        AuthUtils.updateCartItemsCount(principal, user.getCartItemsCount());
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping(value = "/cart", params = {"removeItem"})
    public String removeItemFromCart(final @RequestParam Long offeringId, final HttpServletRequest request, final Principal principal) {
        User user = userService.removeItemFromCart(principal.getName(), offeringId);
        AuthUtils.updateCartItemsCount(principal, user.getCartItemsCount());
        return "redirect:" + request.getHeader("Referer");
    }

    /* ---------------------------------------------- OFFERING ---------------------------------------------- */

    @GetMapping({"/shop/offering/{offeringId}"})
    public String offering(final @PathVariable Long offeringId, final ModelMap model, final Principal principal) {
        ShopItemOfferingResponseDto shopItemOfferingDto = offeringMapper.toShopItemResponseDto(
                offeringService.findByIdFetchProgramsAndFolders(offeringId),
                userService.findByEmailFetchCartItemsAndOrders(principal.getName(),
                        () -> orderService.findAllByUserEmail(principal.getName())));
        model.addAttribute(ModelMapConstants.OFFERING, shopItemOfferingDto);
        model.addAttribute(ModelMapConstants.NOW, LocalDateTime.now());
        return "client/shop/offering";
    }

}
