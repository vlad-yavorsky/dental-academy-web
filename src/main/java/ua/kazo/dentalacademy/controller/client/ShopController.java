package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
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
import ua.kazo.dentalacademy.config.CustomLiqPay;
import ua.kazo.dentalacademy.config.LiqPayProperties;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.offering.ShopItemOfferingResponseDto;
import ua.kazo.dentalacademy.dto.purchase.CartItemDto;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.mapper.OfferingMapper;
import ua.kazo.dentalacademy.mapper.OrderMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.mapper.UserMapper;
import ua.kazo.dentalacademy.security.Permission;
import ua.kazo.dentalacademy.security.TargetType;
import ua.kazo.dentalacademy.service.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private final LiqPayProperties liqPayProperties;
    private final CustomLiqPay liqPay;

    /* ---------------------------------------------- SHOP ---------------------------------------------- */

    @GetMapping("/shop")
    public String shop(final ModelMap model, @RequestParam(required = false) final String search,
                       @RequestParam(defaultValue = "0") final int page,
                       @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllByNotDeactivatedOfferings(search, PageRequest.of(page, size));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "client/shop/shop";
    }

    /* ---------------------------------------------- SHOP ITEM ---------------------------------------------- */

    @GetMapping("/shop/program/{programId}")
    public String shopItem(final ModelMap model, @PathVariable final Long programId, final Principal principal) {
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
        Order order = orderService.create(principal.getName(), liqPayProperties.getOrderPrefix(), liqPay::createParamsAndConvertToJson);
        return new RedirectView("/order/" + order.getNumber());
    }

    @PreAuthorize("hasPermission(#orderNumber, '" + TargetType.ORDER + "', '" + Permission.READ + "')")
    @GetMapping("/order/{orderNumber}")
    public String order(@PathVariable final String orderNumber, final ModelMap model) {
        Order order = orderService.findByNumberFetchCompletePurchaseData(orderNumber);
        Map<String, String> params = liqPay.payParams(order);
        String data = liqPay.convertToJsonAndEncodeToBase64(params);
        String signature = liqPay.createSignature(data);
        model.addAttribute("order", orderMapper.toResponseDto(order));
        model.addAttribute("liqpayData", data);
        model.addAttribute("liqpaySignature", signature);
        return "client/shop/order";
    }

    /* ---------------------------------------------- CART ---------------------------------------------- */

    @GetMapping("/cart")
    public String cart(final ModelMap model, final Principal principal) {
        model.addAttribute("userCart", userMapper.toCartDto(userService.findByEmailFetchCartItems(principal.getName())));
        return "client/shop/cart";
    }

    @PostMapping(value = "/cart", params = {"addItem"})
    public String addItemToCart(final CartItemDto cartItemDto, final ModelMap model, final Principal principal) {
        userService.addItemToCart(principal.getName(), offeringService.findByIdAndActive(cartItemDto.getOfferingId()));
        return cart(model, principal);
    }

    @PostMapping(value = "/cart", params = {"removeItem"})
    public String removeItemFromCart(final CartItemDto cartItemDto, final ModelMap model, final Principal principal) {
        userService.removeItemFromCart(principal.getName(), cartItemDto.getOfferingId());
        return cart(model, principal);
    }

}
