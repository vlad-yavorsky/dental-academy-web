package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.properties.payment.PaymentProperties;
import ua.kazo.dentalacademy.repository.OrderRepository;
import ua.kazo.dentalacademy.service.payment.convertor.PaymentStatusConverterHolder;
import ua.kazo.dentalacademy.service.payment.processor.PaymentProcessor;
import ua.kazo.dentalacademy.service.payment.processor.PaymentProcessorHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OfferingService offeringService;
    private final OrderHistoryService orderHistoryService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final PaymentProperties paymentProperties;

    public Order findByNumber(String number) {
        return orderRepository.findByNumber(number)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.ORDER_NOT_FOUND, number));
    }

    public Order findByNumberFetchPurchaseData(String number) {
        return orderRepository.findFetchPurchaseDataByNumber(number)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.ORDER_NOT_FOUND, number));
    }

    public List<Order> findAllByUserEmail(String email) {
        return orderRepository.findAllByUserEmail(email);
    }

    public Page<Order> findAllFetchUserRoles(Pageable pageable) {
        return orderRepository.findAllFetchUserRolesBy(pageable);
    }

    public Order findByNumberFetchCompletePurchaseData(String number, boolean fetchUser) {
        Order order = (fetchUser ? orderRepository.findFetchPurchaseDataAndOfferingAndUserWithRolesByNumber(number) :
                orderRepository.findFetchPurchaseDataAndOfferingByNumber(number))
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.ORDER_NOT_FOUND, number));
        Set<Long> offeringIds = order.getPurchaseData().stream()
                .map(purchaseData -> purchaseData.getOffering().getId()).collect(Collectors.toSet());
        offeringService.findAllByIdInFetchProgramsAndFolders(offeringIds);
        return order;
    }

    public Page<Order> findAllByUserEmail(String email, Pageable pageable) {
        Page<Order> result = orderRepository.findAllByUserEmail(email, pageable);
        List<Long> ids = result.stream().map(Order::getId).collect(Collectors.toList());
        orderRepository.findAllByIdIn(ids);
        Set<Long> offeringIds = new HashSet<>();
        result.forEach(order -> order.getPurchaseData().forEach(purchaseData -> offeringIds.add(purchaseData.getOffering().getId())));
        offeringService.findAllByIdInFetchProgramsAndFolders(offeringIds);
        return result;
    }

    public Order create(String email) {
        User user = userService.findByEmailFetchCartItems(email);
        Order order = new Order();
        order.setNumber("");
        LocalDateTime now = LocalDateTime.now();
        order.setCreated(now);
        order.setUser(user);
        order.setProvider(paymentProperties.getProvider());
        order.setStatus(UnifiedPaymentStatus.CREATED);
        user.getCartItems().forEach(cartItem -> {
            if (cartItem.getDeactivated() != null && cartItem.getDeactivated().isBefore(now)) {
                throw new ApplicationException(messageSource, ExceptionCode.OFFERING_NOT_AVAILABLE, cartItem.getName());
            }
            order.getPurchaseData().add(PurchaseData.of(cartItem, order));
        });
        order.setPrice(order.getPurchaseData().stream()
                .map(PurchaseData::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO));
        Order savedOrder = orderRepository.save(order);
        savedOrder.setNumber(paymentProperties.getOrderPrefix() + savedOrder.getId());
        user.getCartItems().clear();
        PaymentProcessor paymentProcessor = PaymentProcessorHolder.get(paymentProperties.getProvider());
        orderHistoryService.create(order, paymentProcessor.getParamsAsString(savedOrder));
        return savedOrder;
    }

    public void manualUpdateOrder(String number, UnifiedPaymentStatus unifiedStatus, String data) {
        Order order = findByNumberFetchPurchaseData(number);
        updateOrderInternal(unifiedStatus, data, order);
        if (UnifiedPaymentStatus.SUCCESS != unifiedStatus) {
            order.setPurchased(null);
            order.getPurchaseData().forEach(purchaseData -> purchaseData.setExpired(null));
        }
    }

    public void updateOrder(String number, String status, String data) {
        Order order = findByNumberFetchPurchaseData(number);
        UnifiedPaymentStatus unifiedStatus = PaymentStatusConverterHolder.get(order.getProvider())
                .convertToUnifiedStatus(status);
        updateOrderInternal(unifiedStatus, data, order);
    }

    private void updateOrderInternal(UnifiedPaymentStatus unifiedStatus, String data, Order order) {
        order.setStatus(unifiedStatus);
        orderHistoryService.create(order, data);
        if (UnifiedPaymentStatus.SUCCESS == unifiedStatus) {
            LocalDateTime now = LocalDateTime.now();
            order.setPurchased(now);
            Set<Long> offeringIds = order.getPurchaseData().stream()
                    .map(purchaseData -> purchaseData.getOffering().getId())
                    .collect(Collectors.toSet());
            Map<Long, Byte> offerings = offeringService.findAllById(offeringIds).stream()
                    .collect(Collectors.toMap(Offering::getId, Offering::getTerm));
            order.getPurchaseData().forEach(purchaseData -> purchaseData
                    .setExpired(now.plusDays(offerings.get(purchaseData.getOffering().getId()))));
        }
    }

    public boolean existsByNumberAndUserEmail(String number, String userEmail) {
        return orderRepository.existsByNumberAndUserEmail(number, userEmail);
    }

}
