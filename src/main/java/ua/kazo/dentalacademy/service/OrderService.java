package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OfferingService offeringService;
    private final UserService userService;
    private final MessageSource messageSource;

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.ORDER_NOT_FOUND, id));
    }

    public List<Order> findAllByUserEmail(String email) {
        return orderRepository.findAllByUserEmail(email);
    }

    public Order findByIdFetchPurchaseData(Long id) {
        Order order = orderRepository.findFetchPurchaseDataById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.ORDER_NOT_FOUND, id));
        Set<Long> offeringIds = new HashSet<>();
        order.getPurchaseData().forEach(purchaseData -> offeringIds.add(purchaseData.getOffering().getId()));
        offeringService.findAllByIdInFetchProgramsAndFolders(offeringIds);
        return order;
    }

    public Page<Order> findAllByUserEmail(String email, Pageable pageable) {
        Page<Order> result = orderRepository.findAllByUserEmail(email, pageable);
        Set<Long> offeringIds = new HashSet<>();
        result.forEach(order -> order.getPurchaseData().forEach(purchaseData -> offeringIds.add(purchaseData.getOffering().getId())));
        offeringService.findAllByIdInFetchProgramsAndFolders(offeringIds);
        return result;
    }

    public Order create(String email) {
        User user = userService.findByEmailFetchCartItems(email);
        // todo: check if every offering is not deactivated
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order();
        order.setUser(user);
        order.setStatus(LiqPayPaymentStatus.WAITING_FOR_PAYMENT);
        order.setPurchased(now);
        user.getCartItems().forEach(offering -> order.getPurchaseData().add(PurchaseData.of(offering, order, now)));
        order.setPrice(order.getPurchaseData().stream()
                .map(PurchaseData::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO));
        Order savedOrder = orderRepository.save(order);
        user.getCartItems().clear();
        return savedOrder;
    }

    public void setStatus(Long orderId, String status) {
        findById(orderId).setStatus(LiqPayPaymentStatus.of(status));
    }

    public boolean existsByIdAndUserEmail(Long id, String userEmail) {
        return orderRepository.existsByIdAndUserEmail(id, userEmail);
    }

}
