package ua.kazo.dentalacademy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.kazo.dentalacademy.constants.Graph;
import ua.kazo.dentalacademy.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByNumber(String number);

    /**
     * Client / Order
     */
    @EntityGraph(Graph.ORDER_PURCHASE_DATA_OFFERING)
    Optional<Order> findFetchPurchaseDataByNumber(String number);

    /**
     * Client / Orders
     */
    @EntityGraph(Graph.ORDER_PURCHASE_DATA_OFFERING)
    Page<Order> findAllByUserEmail(String email, Pageable pageable);

    /**
     * Client / Shop Item
     */
    @EntityGraph(Graph.ORDER_PURCHASE_DATA_OFFERING)
    List<Order> findAllByUserEmail(String email);

    /**
     * Client / Permission Evaluator
     */
    boolean existsByNumberAndUserEmail(String number, String email);

}
