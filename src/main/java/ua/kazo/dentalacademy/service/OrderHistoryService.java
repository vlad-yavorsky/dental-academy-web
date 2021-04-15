package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.entity.OrderHistory;
import ua.kazo.dentalacademy.repository.OrderHistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistory create(Order order, String data) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatus(order.getStatus());
        orderHistory.setData(data);
        return orderHistoryRepository.save(orderHistory);
    }

    public List<OrderHistory> findAllByOrderId(Long orderId) {
        return orderHistoryRepository.findAllByOrderIdOrderByCreatedAsc(orderId);
    }

}
