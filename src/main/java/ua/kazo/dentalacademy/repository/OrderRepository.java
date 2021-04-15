package ua.kazo.dentalacademy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.constants.Graph;
import ua.kazo.dentalacademy.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByNumber(String number);

    @EntityGraph(attributePaths = "purchaseData")
    Optional<Order> findFetchPurchaseDataByNumber(String number);

    /**
     * Client / Order
     */
    @EntityGraph(Graph.ORDER_PURCHASE_DATA_OFFERING)
    Optional<Order> findFetchPurchaseDataAndOfferingByNumber(String number);

    /**
     * Admin / Orders
     */
    @EntityGraph(attributePaths = {"user.roles"})
    Page<Order> findAllFetchUserRolesBy(Pageable pageable);

    /**
     * Admin / Order
     */
    @EntityGraph(value = Graph.ORDER_PURCHASE_DATA_OFFERING_AND_USER_WITH_ROLES)
    Optional<Order> findFetchPurchaseDataAndOfferingAndUserWithRolesByNumber(String number);

    /**
     * Client / Orders
     */
    Page<Order> findAllByUserEmail(String email, Pageable pageable);

    /**
     * Client / Orders
     */
    @EntityGraph(Graph.ORDER_PURCHASE_DATA_OFFERING)
    List<Order> findAllByIdIn(List<Long> ids);

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
